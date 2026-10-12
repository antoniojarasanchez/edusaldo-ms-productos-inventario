package cl.edusaldo.productos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class DescontarStockDTO {

    @Min(value = 1, message = "La cantidad a descontar debe ser al menos 1")
    private int cantidad;

    @NotNull(message = "El ID de referencia es obligatorio")
    private Long referenciaId;

    @NotBlank(message = "La llave de idempotencia es obligatoria")
    private String idempotencyKey;

    @NotNull(message = "El ID de usuario es obligatorio")
    private Long usuarioId;

    public DescontarStockDTO() {}

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public Long getReferenciaId() { return referenciaId; }
    public void setReferenciaId(Long referenciaId) { this.referenciaId = referenciaId; }
    public String getIdempotencyKey() { return idempotencyKey; }
    public void setIdempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
}
