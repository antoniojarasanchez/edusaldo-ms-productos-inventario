package cl.edusaldo.productos.dto;

import cl.edusaldo.productos.model.EstadoAutorizacion;
import cl.edusaldo.productos.model.SolicitudAjusteStock;

import java.time.LocalDateTime;

public class SolicitudAjusteResponseDTO {
    private Long id;
    private Long productoId;
    private String nombreProducto;
    private int cantidad;
    private String motivo;
    private String evidenciaUrl;
    private Long solicitanteId;
    private Long autorizadorId;
    private EstadoAutorizacion estado;
    private LocalDateTime fechaSolicitud;
    private LocalDateTime fechaResolucion;
    private String comentarioResolucion;

    public SolicitudAjusteResponseDTO() {}

    public SolicitudAjusteResponseDTO(SolicitudAjusteStock s) {
        this.id = s.getId();
        if (s.getProducto() != null) {
            this.productoId = s.getProducto().getId();
            this.nombreProducto = s.getProducto().getNombre();
        }
        this.cantidad = s.getCantidad();
        this.motivo = s.getMotivo();
        this.evidenciaUrl = s.getEvidenciaUrl();
        this.solicitanteId = s.getSolicitanteId();
        this.autorizadorId = s.getAutorizadorId();
        this.estado = s.getEstado();
        this.fechaSolicitud = s.getFechaSolicitud();
        this.fechaResolucion = s.getFechaResolucion();
        this.comentarioResolucion = s.getComentarioResolucion();
    }

    public Long getId() { return id; }
    public Long getProductoId() { return productoId; }
    public String getNombreProducto() { return nombreProducto; }
    public int getCantidad() { return cantidad; }
    public String getMotivo() { return motivo; }
    public String getEvidenciaUrl() { return evidenciaUrl; }
    public Long getSolicitanteId() { return solicitanteId; }
    public Long getAutorizadorId() { return autorizadorId; }
    public EstadoAutorizacion getEstado() { return estado; }
    public LocalDateTime getFechaSolicitud() { return fechaSolicitud; }
    public LocalDateTime getFechaResolucion() { return fechaResolucion; }
    public String getComentarioResolucion() { return comentarioResolucion; }
}
