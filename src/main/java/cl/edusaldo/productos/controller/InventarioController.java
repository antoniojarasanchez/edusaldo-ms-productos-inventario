package cl.edusaldo.productos.controller;

import cl.edusaldo.productos.dto.*;
import cl.edusaldo.productos.model.SolicitudAjusteStock;
import cl.edusaldo.productos.service.InventarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/inventario")
@Tag(name = "Inventario y Stock", description = "Operaciones de stock, reservas, recepciones y ajustes con doble firma")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @PostMapping("/{productoId}/reservar")
    @Operation(summary = "Reservar stock por pedido")
    public ResponseEntity<Map<String, String>> reservar(
            @PathVariable Long productoId,
            @Valid @RequestBody ReservarStockDTO dto) {
        inventarioService.reservar(productoId, dto);
        return ResponseEntity.ok(Map.of("message", "Stock reservado exitosamente"));
    }

    @PostMapping("/{productoId}/descontar")
    @Operation(summary = "Descontar stock físico por entrega o venta")
    public ResponseEntity<Map<String, String>> descontar(
            @PathVariable Long productoId,
            @Valid @RequestBody DescontarStockDTO dto) {
        inventarioService.descontar(productoId, dto);
        return ResponseEntity.ok(Map.of("message", "Stock descontado exitosamente"));
    }

    @PostMapping("/{productoId}/entrada-compra")
    @Operation(summary = "Aumentar stock y recalcular CPP por recepción aceptada de compras")
    public ResponseEntity<Map<String, String>> entradaCompra(
            @PathVariable Long productoId,
            @Valid @RequestBody EntradaCompraDTO dto) {
        inventarioService.registrarEntradaCompra(productoId, dto);
        return ResponseEntity.ok(Map.of("message", "Entrada registrada y CPP recalculado exitosamente"));
    }

    @PostMapping("/ajustes/solicitar")
    @Operation(summary = "Crear solicitud de ajuste de stock (Admin)")
    public ResponseEntity<SolicitudAjusteResponseDTO> solicitarAjuste(@Valid @RequestBody SolicitarAjusteDTO dto) {
        SolicitudAjusteStock sol = inventarioService.solicitarAjuste(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SolicitudAjusteResponseDTO(sol));
    }

    @PutMapping("/ajustes/{solicitudId}/autorizar")
    @Operation(summary = "Aprobar o rechazar ajuste de stock (Autorizador - Segunda firma)")
    public ResponseEntity<SolicitudAjusteResponseDTO> autorizarAjuste(
            @PathVariable Long solicitudId,
            @Valid @RequestBody AprobarAjusteDTO dto) {
        SolicitudAjusteStock sol = inventarioService.autorizarAjuste(solicitudId, dto);
        return ResponseEntity.ok(new SolicitudAjusteResponseDTO(sol));
    }
}
