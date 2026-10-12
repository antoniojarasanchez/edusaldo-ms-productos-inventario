package cl.edusaldo.productos.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "solicitudes_ajuste_stock")
public class SolicitudAjusteStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(name = "cantidad", nullable = false)
    private int cantidad; // positivo (sobrante) o negativo (merma/pérdida)

    @Column(name = "motivo", nullable = false)
    private String motivo;

    @Column(name = "evidencia_url")
    private String evidenciaUrl;

    @Column(name = "solicitante_id", nullable = false)
    private Long solicitanteId; // Administrador

    @Column(name = "autorizador_id")
    private Long autorizadorId; // Autorizador (segunda firma)

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 30)
    private EstadoAutorizacion estado = EstadoAutorizacion.PENDIENTE;

    @Column(name = "fecha_solicitud", nullable = false)
    private LocalDateTime fechaSolicitud = LocalDateTime.now();

    @Column(name = "fecha_resolucion")
    private LocalDateTime fechaResolucion;

    @Column(name = "comentario_resolucion")
    private String comentarioResolucion;

    @Column(name = "reporte_diferencia_id")
    private Long reporteDiferenciaId;

    public SolicitudAjusteStock() {}

    public SolicitudAjusteStock(Producto producto, int cantidad, String motivo, String evidenciaUrl, Long solicitanteId, Long reporteDiferenciaId) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.motivo = motivo;
        this.evidenciaUrl = evidenciaUrl;
        this.solicitanteId = solicitanteId;
        this.reporteDiferenciaId = reporteDiferenciaId;
        this.estado = EstadoAutorizacion.PENDIENTE;
        this.fechaSolicitud = LocalDateTime.now();
    }

    public void aprobar(Long autorizadorId) {
        this.estado = EstadoAutorizacion.APROBADA;
        this.autorizadorId = autorizadorId;
        this.fechaResolucion = LocalDateTime.now();
    }

    public void rechazar(Long autorizadorId, String comentario) {
        this.estado = EstadoAutorizacion.RECHAZADA;
        this.autorizadorId = autorizadorId;
        this.comentarioResolucion = comentario;
        this.fechaResolucion = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Producto getProducto() { return producto; }
    public int getCantidad() { return cantidad; }
    public String getMotivo() { return motivo; }
    public String getEvidenciaUrl() { return evidenciaUrl; }
    public Long getSolicitanteId() { return solicitanteId; }
    public Long getAutorizadorId() { return autorizadorId; }
    public EstadoAutorizacion getEstado() { return estado; }
    public LocalDateTime getFechaSolicitud() { return fechaSolicitud; }
    public LocalDateTime getFechaResolucion() { return fechaResolucion; }
    public String getComentarioResolucion() { return comentarioResolucion; }
    public Long getReporteDiferenciaId() { return reporteDiferenciaId; }
}
