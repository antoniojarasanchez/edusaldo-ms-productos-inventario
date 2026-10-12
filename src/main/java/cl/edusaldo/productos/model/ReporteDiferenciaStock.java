package cl.edusaldo.productos.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reportes_diferencia_stock")
public class ReporteDiferenciaStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(name = "cantidad_diferencia", nullable = false)
    private int cantidadDiferencia;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "evidencia_url")
    private String evidenciaUrl;

    @Column(name = "reportado_por_id", nullable = false)
    private Long reportadoPorId; // Encargado

    @Column(name = "fecha_reporte", nullable = false)
    private LocalDateTime fechaReporte = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 30)
    private EstadoReporte estado = EstadoReporte.PENDIENTE;

    @Column(name = "atendido_por_id")
    private Long atendidoPorId; // Admin

    @Column(name = "observacion")
    private String observacion;

    public ReporteDiferenciaStock() {}

    public ReporteDiferenciaStock(Producto producto, int cantidadDiferencia, String descripcion, String evidenciaUrl, Long reportadoPorId) {
        this.producto = producto;
        this.cantidadDiferencia = cantidadDiferencia;
        this.descripcion = descripcion;
        this.evidenciaUrl = evidenciaUrl;
        this.reportadoPorId = reportadoPorId;
        this.estado = EstadoReporte.PENDIENTE;
        this.fechaReporte = LocalDateTime.now();
    }

    public void marcarAtendido(Long adminId) {
        this.estado = EstadoReporte.ATENDIDO;
        this.atendidoPorId = adminId;
    }

    public void descartar(Long adminId, String motivo) {
        this.estado = EstadoReporte.DESCARTADO;
        this.atendidoPorId = adminId;
        this.observacion = motivo;
    }

    public Long getId() { return id; }
    public Producto getProducto() { return producto; }
    public int getCantidadDiferencia() { return cantidadDiferencia; }
    public String getDescripcion() { return descripcion; }
    public String getEvidenciaUrl() { return evidenciaUrl; }
    public Long getReportadoPorId() { return reportadoPorId; }
    public LocalDateTime getFechaReporte() { return fechaReporte; }
    public EstadoReporte getEstado() { return estado; }
    public Long getAtendidoPorId() { return atendidoPorId; }
    public String getObservacion() { return observacion; }
}
