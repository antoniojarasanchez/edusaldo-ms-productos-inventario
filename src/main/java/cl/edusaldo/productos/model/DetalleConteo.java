package cl.edusaldo.productos.model;

import jakarta.persistence.*;

@Entity
@Table(name = "detalles_conteo")
public class DetalleConteo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventario_fisico_id", nullable = false)
    private InventarioFisico inventarioFisico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(name = "stock_sistema", nullable = false)
    private int stockSistema;

    @Column(name = "stock_contado", nullable = false)
    private int stockContado;

    public DetalleConteo() {}

    public DetalleConteo(InventarioFisico inventarioFisico, Producto producto, int stockSistema, int stockContado) {
        this.inventarioFisico = inventarioFisico;
        this.producto = producto;
        this.stockSistema = stockSistema;
        this.stockContado = stockContado;
    }

    public int getDiferencia() {
        return this.stockContado - this.stockSistema;
    }

    public boolean tieneDiferencia() {
        return getDiferencia() != 0;
    }

    public Long getId() { return id; }
    public InventarioFisico getInventarioFisico() { return inventarioFisico; }
    public Producto getProducto() { return producto; }
    public int getStockSistema() { return stockSistema; }
    public int getStockContado() { return stockContado; }
}
