package cl.edusaldo.productos.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimientos_inventario")
public class MovimientoInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 50)
    private TipoMovimientoInventario tipo;

    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    @Column(name = "costo_unitario", nullable = false)
    private Long costoUnitario;

    @Column(name = "stock_fisico_resultante", nullable = false)
    private int stockFisicoResultante;

    @Column(name = "stock_reservado_resultante", nullable = false)
    private int stockReservadoResultante;

    @Column(name = "cpp_resultante", nullable = false)
    private Long cppResultante;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();

    @Column(name = "referencia_tipo", nullable = false, length = 50)
    private String referenciaTipo;

    @Column(name = "referencia_id")
    private Long referenciaId;

    @Column(name = "idempotency_key", nullable = false, unique = true, length = 100)
    private String idempotencyKey;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId; // Ref escalar a MS Auth

    public MovimientoInventario() {}

    public MovimientoInventario(Producto producto, TipoMovimientoInventario tipo, int cantidad, Long costoUnitario,
                                int stockFisicoResultante, int stockReservadoResultante, Long cppResultante,
                                String referenciaTipo, Long referenciaId, String idempotencyKey, Long usuarioId) {
        this.producto = producto;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.costoUnitario = costoUnitario;
        this.stockFisicoResultante = stockFisicoResultante;
        this.stockReservadoResultante = stockReservadoResultante;
        this.cppResultante = cppResultante;
        this.referenciaTipo = referenciaTipo;
        this.referenciaId = referenciaId;
        this.idempotencyKey = idempotencyKey;
        this.usuarioId = usuarioId;
        this.fecha = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Producto getProducto() { return producto; }
    public TipoMovimientoInventario getTipo() { return tipo; }
    public int getCantidad() { return cantidad; }
    public Long getCostoUnitario() { return costoUnitario; }
    public int getStockFisicoResultante() { return stockFisicoResultante; }
    public int getStockReservadoResultante() { return stockReservadoResultante; }
    public Long getCppResultante() { return cppResultante; }
    public LocalDateTime getFecha() { return fecha; }
    public String getReferenciaTipo() { return referenciaTipo; }
    public Long getReferenciaId() { return referenciaId; }
    public String getIdempotencyKey() { return idempotencyKey; }
    public Long getUsuarioId() { return usuarioId; }
}
