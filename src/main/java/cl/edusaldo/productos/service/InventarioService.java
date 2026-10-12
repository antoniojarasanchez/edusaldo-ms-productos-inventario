package cl.edusaldo.productos.service;

import cl.edusaldo.productos.dto.*;
import cl.edusaldo.productos.model.*;
import cl.edusaldo.productos.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventarioService {

    private final ProductoRepository productoRepository;
    private final MovimientoInventarioRepository movimientoInventarioRepository;
    private final SolicitudAjusteStockRepository solicitudAjusteStockRepository;

    public InventarioService(ProductoRepository productoRepository,
                             MovimientoInventarioRepository movimientoInventarioRepository,
                             SolicitudAjusteStockRepository solicitudAjusteStockRepository) {
        this.productoRepository = productoRepository;
        this.movimientoInventarioRepository = movimientoInventarioRepository;
        this.solicitudAjusteStockRepository = solicitudAjusteStockRepository;
    }

    @Transactional
    public void reservar(Long productoId, ReservarStockDTO dto) {
        if (movimientoInventarioRepository.existsByIdempotencyKey(dto.getIdempotencyKey())) {
            return;
        }

        Producto p = productoRepository.findById(productoId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + productoId));

        p.reservar(dto.getCantidad());
        productoRepository.save(p);

        MovimientoInventario mov = new MovimientoInventario(
                p, TipoMovimientoInventario.RESERVA, dto.getCantidad(), p.getCpp(),
                p.getStockFisico(), p.getStockReservado(), p.getCpp(),
                "RESERVA", dto.getReferenciaId(), dto.getIdempotencyKey(), dto.getUsuarioId()
        );
        movimientoInventarioRepository.save(mov);
    }

    @Transactional
    public void descontar(Long productoId, DescontarStockDTO dto) {
        if (movimientoInventarioRepository.existsByIdempotencyKey(dto.getIdempotencyKey())) {
            return;
        }

        Producto p = productoRepository.findById(productoId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + productoId));

        p.descontar(dto.getCantidad());
        productoRepository.save(p);

        MovimientoInventario mov = new MovimientoInventario(
                p, TipoMovimientoInventario.SALIDA_VENTA, dto.getCantidad(), p.getCpp(),
                p.getStockFisico(), p.getStockReservado(), p.getCpp(),
                "VENTA", dto.getReferenciaId(), dto.getIdempotencyKey(), dto.getUsuarioId()
        );
        movimientoInventarioRepository.save(mov);
    }

    @Transactional
    public void registrarEntradaCompra(Long productoId, EntradaCompraDTO dto) {
        if (movimientoInventarioRepository.existsByIdempotencyKey(dto.getIdempotencyKey())) {
            return;
        }

        Producto p = productoRepository.findById(productoId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + productoId));

        // Invariante de recálculo de CPP con IVA incluido (HU51)
        p.registrarEntrada(dto.getCantidad(), dto.getCostoUnitarioConIva());
        productoRepository.save(p);

        MovimientoInventario mov = new MovimientoInventario(
                p, TipoMovimientoInventario.ENTRADA_COMPRA, dto.getCantidad(), dto.getCostoUnitarioConIva(),
                p.getStockFisico(), p.getStockReservado(), p.getCpp(),
                "RECEPCION_COMPRA", dto.getRecepcionId(), dto.getIdempotencyKey(), dto.getUsuarioId()
        );
        movimientoInventarioRepository.save(mov);
    }

    @Transactional
    public SolicitudAjusteStock solicitarAjuste(SolicitarAjusteDTO dto) {
        Producto p = productoRepository.findById(dto.getProductoId())
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + dto.getProductoId()));

        SolicitudAjusteStock sol = new SolicitudAjusteStock(
                p, dto.getCantidad(), dto.getMotivo(), dto.getEvidenciaUrl(),
                dto.getSolicitanteId(), dto.getReporteDiferenciaId()
        );
        return solicitudAjusteStockRepository.save(sol);
    }

    @Transactional
    public SolicitudAjusteStock autorizarAjuste(Long solicitudId, AprobarAjusteDTO dto) {
        SolicitudAjusteStock sol = solicitudAjusteStockRepository.findById(solicitudId)
                .orElseThrow(() -> new IllegalArgumentException("Solicitud de ajuste no encontrada con ID: " + solicitudId));

        if (sol.getEstado() != EstadoAutorizacion.PENDIENTE) {
            throw new IllegalStateException("La solicitud ya fue resuelta previamente.");
        }

        if (Boolean.TRUE.equals(dto.getAprobada())) {
            sol.aprobar(dto.getAutorizadorId());
            Producto p = sol.getProducto();
            p.ajustarStock(sol.getCantidad());
            productoRepository.save(p);

            TipoMovimientoInventario tipo = sol.getCantidad() > 0 ? TipoMovimientoInventario.AJUSTE_POSITIVO : TipoMovimientoInventario.AJUSTE_NEGATIVO;
            MovimientoInventario mov = new MovimientoInventario(
                    p, tipo, Math.abs(sol.getCantidad()), p.getCpp(),
                    p.getStockFisico(), p.getStockReservado(), p.getCpp(),
                    "AJUSTE_STOCK", sol.getId(), "AJUSTE-" + sol.getId() + "-" + System.currentTimeMillis(), dto.getAutorizadorId()
            );
            movimientoInventarioRepository.save(mov);
        } else {
            sol.rechazar(dto.getAutorizadorId(), dto.getComentario());
        }

        return solicitudAjusteStockRepository.save(sol);
    }
}
