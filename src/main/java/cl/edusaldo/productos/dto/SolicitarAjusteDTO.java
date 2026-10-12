package cl.edusaldo.productos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SolicitarAjusteDTO {

    @NotNull(message = "El ID del producto es obligatorio")
    private Long productoId;

    @NotNull(message = "La cantidad del ajuste es obligatoria")
    private Integer cantidad; // positivo o negativo

    @NotBlank(message = "El motivo del ajuste es obligatorio")
    private String motivo;

    private String evidenciaUrl;

    @NotNull(message = "El ID del solicitante (Admin) es obligatorio")
    private Long solicitanteId;

    private Long reporteDiferenciaId;

    public SolicitarAjusteDTO() {}

    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public String getEvidenciaUrl() { return evidenciaUrl; }
    public void setEvidenciaUrl(String evidenciaUrl) { this.evidenciaUrl = evidenciaUrl; }
    public Long getSolicitanteId() { return solicitanteId; }
    public void setSolicitanteId(Long solicitanteId) { this.solicitanteId = solicitanteId; }
    public Long getReporteDiferenciaId() { return reporteDiferenciaId; }
    public void setReporteDiferenciaId(Long reporteDiferenciaId) { this.reporteDiferenciaId = reporteDiferenciaId; }
}
