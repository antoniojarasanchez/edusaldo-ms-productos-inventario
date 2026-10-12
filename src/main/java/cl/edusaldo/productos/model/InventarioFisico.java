package cl.edusaldo.productos.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "inventarios_fisicos")
public class InventarioFisico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_conteo", nullable = false)
    private LocalDate fechaConteo;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @Column(name = "responsable_id", nullable = false)
    private Long responsableId; // Encargado

    @Column(name = "categoria_filtro", length = 100)
    private String categoriaFiltro;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "respaldo_url")
    private String respaldoUrl; // Ruta del PDF/foto escaneada firmada

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 30)
    private EstadoInventario estado = EstadoInventario.EN_CURSO;

    @OneToMany(mappedBy = "inventarioFisico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleConteo> detalles = new ArrayList<>();

    public InventarioFisico() {}

    public InventarioFisico(LocalDate fechaConteo, Long responsableId, String categoriaFiltro, String observaciones, String respaldoUrl) {
        this.fechaConteo = fechaConteo;
        this.responsableId = responsableId;
        this.categoriaFiltro = categoriaFiltro;
        this.observaciones = observaciones;
        this.respaldoUrl = respaldoUrl;
        this.estado = EstadoInventario.EN_CURSO;
        this.fechaRegistro = LocalDateTime.now();
    }

    public void finalizar() {
        this.estado = EstadoInventario.FINALIZADO;
    }

    public Long getId() { return id; }
    public LocalDate getFechaConteo() { return fechaConteo; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public Long getResponsableId() { return responsableId; }
    public String getCategoriaFiltro() { return categoriaFiltro; }
    public String getObservaciones() { return observaciones; }
    public String getRespaldoUrl() { return respaldoUrl; }
    public EstadoInventario getEstado() { return estado; }
    public List<DetalleConteo> getDetalles() { return detalles; }
}
