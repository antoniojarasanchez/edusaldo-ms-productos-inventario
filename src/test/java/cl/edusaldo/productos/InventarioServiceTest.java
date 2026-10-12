package cl.edusaldo.productos;

import cl.edusaldo.productos.dto.AprobarAjusteDTO;
import cl.edusaldo.productos.dto.EntradaCompraDTO;
import cl.edusaldo.productos.dto.ReservarStockDTO;
import cl.edusaldo.productos.dto.SolicitarAjusteDTO;
import cl.edusaldo.productos.model.*;
import cl.edusaldo.productos.repository.MovimientoInventarioRepository;
import cl.edusaldo.productos.repository.ProductoRepository;
import cl.edusaldo.productos.repository.SolicitudAjusteStockRepository;
import cl.edusaldo.productos.service.InventarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventarioServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private MovimientoInventarioRepository movimientoInventarioRepository;

    @Mock
    private SolicitudAjusteStockRepository solicitudAjusteStockRepository;

    @InjectMocks
    private InventarioService inventarioService;

    private Categoria categoriaPrueba;
    private Producto productoPrueba;

    @BeforeEach
    void setUp() {
        categoriaPrueba = new Categoria("Cuadernos", "Cuadernos escolares");
        categoriaPrueba.setId(1L);

        // Producto inicial: 10 unidades físicas, 0 reservadas, CPP $1.000
        productoPrueba = new Producto(
                categoriaPrueba, "CUAD-UNI-01", "7800000000101", "Cuaderno universitario",
                "Todos", "General", 1250L, 1000L, 10, 5
        );
        productoPrueba.setId(101L);
    }

    @Test
    @DisplayName("1. Rechazo de reserva por stock insuficiente (Invariante HU41)")
    void testRechazoReservaStockInsuficiente() {
        when(productoRepository.findById(101L)).thenReturn(Optional.of(productoPrueba));
        when(movimientoInventarioRepository.existsByIdempotencyKey("KEY-INV-01")).thenReturn(false);

        ReservarStockDTO dto = new ReservarStockDTO();
        dto.setCantidad(15); // Mayor que stock disponible de 10
        dto.setReferenciaId(1L);
        dto.setUsuarioId(4L);
        dto.setIdempotencyKey("KEY-INV-01");

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> inventarioService.reservar(101L, dto));
        assertTrue(ex.getMessage().contains("Stock insuficiente"));
        assertEquals(10, productoPrueba.getStockFisico());
        assertEquals(0, productoPrueba.getStockReservado());
        assertEquals(10, productoPrueba.getStockDisponible());
    }

    @Test
    @DisplayName("2. Recálculo aritmético exacto de CPP con IVA incluido (Regla HU51)")
    void testCalculoMatematicoCppConIva() {
        // Inicial: 10 unidades con CPP $1.000 (Costo total $10.000)
        assertEquals(1000L, productoPrueba.getCpp());
        assertEquals(10, productoPrueba.getStockFisico());

        when(productoRepository.findById(101L)).thenReturn(Optional.of(productoPrueba));
        when(movimientoInventarioRepository.existsByIdempotencyKey("KEY-BUY-01")).thenReturn(false);
        when(productoRepository.save(any(Producto.class))).thenAnswer(i -> i.getArgument(0));

        // Entrada nueva de compras: 10 unidades con costo facturado con IVA $1.200 (Total nuevo $12.000)
        EntradaCompraDTO dto = new EntradaCompraDTO();
        dto.setCantidad(10);
        dto.setCostoUnitarioConIva(1200L);
        dto.setRecepcionId(501L);
        dto.setUsuarioId(2L);
        dto.setIdempotencyKey("KEY-BUY-01");

        inventarioService.registrarEntradaCompra(101L, dto);

        // Aserción matemática: (10.000 + 12.000) / 20 = $1.100 exacto
        assertEquals(1100L, productoPrueba.getCpp());
        assertEquals(20, productoPrueba.getStockFisico());
        assertEquals(20, productoPrueba.getStockDisponible());
    }

    @Test
    @DisplayName("3. Doble firma: Solicitud de ajuste no muta stock hasta que el Autorizador aprueba (HU45)")
    void testAjusteStockProtegidoSinSegundaFirma() {
        SolicitudAjusteStock solicitud = new SolicitudAjusteStock(
                productoPrueba, -3, "Merma por caída de líquido", "http://evidencia.jpg", 1L, null
        );
        solicitud.setId(201L);

        when(solicitudAjusteStockRepository.findById(201L)).thenReturn(Optional.of(solicitud));
        when(solicitudAjusteStockRepository.save(any(SolicitudAjusteStock.class))).thenAnswer(i -> i.getArgument(0));

        // El stock inicial sigue en 10 antes de la firma
        assertEquals(10, productoPrueba.getStockFisico());

        // Autorizador rechaza el ajuste
        AprobarAjusteDTO dtoRechazo = new AprobarAjusteDTO();
        dtoRechazo.setAutorizadorId(3L);
        dtoRechazo.setAprobada(false);
        dtoRechazo.setComentario("Evidencia insuficiente");

        inventarioService.autorizarAjuste(201L, dtoRechazo);

        assertEquals(EstadoAutorizacion.RECHAZADA, solicitud.getEstado());
        assertEquals(10, productoPrueba.getStockFisico()); // Stock NO fue alterado

        // Caso alternativo: Autorizador aprueba
        SolicitudAjusteStock solicitud2 = new SolicitudAjusteStock(
                productoPrueba, -3, "Merma comprobada", "http://evidencia2.jpg", 1L, null
        );
        solicitud2.setId(202L);

        when(solicitudAjusteStockRepository.findById(202L)).thenReturn(Optional.of(solicitud2));
        when(productoRepository.save(any(Producto.class))).thenAnswer(i -> i.getArgument(0));

        AprobarAjusteDTO dtoAprobado = new AprobarAjusteDTO();
        dtoAprobado.setAutorizadorId(3L);
        dtoAprobado.setAprobada(true);

        inventarioService.autorizarAjuste(202L, dtoAprobado);

        assertEquals(EstadoAutorizacion.APROBADA, solicitud2.getEstado());
        assertEquals(7, productoPrueba.getStockFisico()); // Stock descontado en 3 tras segunda firma
    }
}
