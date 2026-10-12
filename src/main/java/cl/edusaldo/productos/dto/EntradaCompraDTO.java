package cl.edusaldo.productos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class EntradaCompraDTO {

    @Min(value = 1, message = "La cantidad recibida debe ser mayor a 0")
    private int cantidad;

    @NotNull(message = "El costo unitario con IVA es obligatorio")
    @Positive(message = "El costo unitario con IVA debe ser positivo")
    private Long costoUnitarioConIva;

    @NotNull(message = "El ID de recepción es obligatorio")
    private Long recepcionId;

    @NotBlank(message = "La llave de idempotencia es obligatoria")
    private String idempotencyKey;

    @NotNull(message = "El ID de usuario es obligatorio")
    private Long usuarioId;

    public EntradaCompraDTO() {}

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public Long getCostoUnitarioConIva() { return costoUnitarioConIva; }
    public void setCostoUnitarioConIva(Long costoUnitarioConIva) { this.costoUnitarioConIva = costoUnitarioConIva; }
    public Long getRecepcionId() { return recepcionId; }
    public void setRecepcionId(Long recepcionId) { this.recepcionId = recepcionId; }
    public String getIdempotencyKey() { return idempotencyKey; }
    public void setIdempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
}
