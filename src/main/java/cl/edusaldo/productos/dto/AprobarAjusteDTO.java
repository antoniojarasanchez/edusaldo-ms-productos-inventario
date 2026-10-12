package cl.edusaldo.productos.dto;

import jakarta.validation.constraints.NotNull;

public class AprobarAjusteDTO {

    @NotNull(message = "El ID del autorizador es obligatorio")
    private Long autorizadorId;

    @NotNull(message = "La decisión de aprobación es obligatoria")
    private Boolean aprobada;

    private String comentario;

    public AprobarAjusteDTO() {}

    public Long getAutorizadorId() { return autorizadorId; }
    public void setAutorizadorId(Long autorizadorId) { this.autorizadorId = autorizadorId; }
    public Boolean getAprobada() { return aprobada; }
    public void setAprobada(Boolean aprobada) { this.aprobada = aprobada; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
}
