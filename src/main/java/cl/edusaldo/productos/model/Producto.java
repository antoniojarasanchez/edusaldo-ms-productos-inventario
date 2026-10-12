package cl.edusaldo.productos.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @Column(name = "sku", nullable = false, unique = true, length = 50)
    private String sku;

    @Column(name = "codigo_barras", nullable = false, length = 50)
    private String codigoBarras;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "imagen_url")
    private String imagenUrl;

    @Column(name = "curso_nivel", nullable = false, length = 100)
    private String cursoNivel;

    @Column(name = "materia", nullable = false, length = 100)
    private String materia;

    @Column(name = "precio_venta", nullable = false)
    private Long precioVenta;

    @Column(name = "margen_utilidad", nullable = false, precision = 5, scale = 2)
    private BigDecimal margenUtilidad = new BigDecimal("30.00");

    @Column(name = "cpp", nullable = false)
    private Long cpp; // Costo Promedio Ponderado con IVA incluido (Long)

    @Column(name = "stock_fisico", nullable = false)
    private int stockFisico = 0;

    @Column(name = "stock_reservado", nullable = false)
    private int stockReservado = 0;

    @Column(name = "stock_critico", nullable = false)
    private int stockCritico = 5;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 30)
    private EstadoRegistro estado = EstadoRegistro.ACTIVO;

    public Producto() {}

    public Producto(Categoria categoria, String sku, String codigoBarras, String nombre,
                    String cursoNivel, String materia, Long precioVenta, Long cpp, int stockInicial, int stockCritico) {
        this.categoria = categoria;
        this.sku = sku;
        this.codigoBarras = codigoBarras;
        this.nombre = nombre;
        this.cursoNivel = cursoNivel;
        this.materia = materia;
        this.precioVenta = precioVenta;
        this.cpp = cpp;
        this.stockFisico = stockInicial;
        this.stockReservado = 0;
        this.stockCritico = stockCritico;
        this.estado = EstadoRegistro.ACTIVO;
    }

    public int getStockDisponible() {
        return this.stockFisico - this.stockReservado;
    }

    public boolean esCritico() {
        return getStockDisponible() <= this.stockCritico;
    }

    // Invariante de reserva: no permite sobreventa
    public void reservar(int cant) {
        if (cant > getStockDisponible()) {
            throw new IllegalStateException("Stock insuficiente para reservar " + cant + " unidades. Disponible: " + getStockDisponible());
        }
        this.stockReservado += cant;
    }

    public void liberar(int cant) {
        this.stockReservado = Math.max(0, this.stockReservado - cant);
    }

    public void descontar(int cant) {
        if (cant > this.stockFisico) {
            throw new IllegalStateException("Stock físico insuficiente para descontar.");
        }
        this.stockFisico -= cant;
        this.stockReservado = Math.max(0, this.stockReservado - cant);
    }

    public void reponer(int cant) {
        this.stockFisico += cant;
    }

    // Invariante de cálculo de CPP con IVA incluido (HU51)
    public void registrarEntrada(int cant, Long costoConIva) {
        if (cant <= 0) return;
        long costoStockActual = (long) this.stockFisico * this.cpp;
        long costoStockNuevo = (long) cant * costoConIva;
        int nuevoStockFisico = this.stockFisico + cant;

        this.cpp = Math.round((double)(costoStockActual + costoStockNuevo) / nuevoStockFisico);
        this.stockFisico = nuevoStockFisico;
    }

    // Ajuste de stock autorizado (segunda firma HU45)
    public void ajustarStock(int cantAjuste) {
        int nuevoFisico = this.stockFisico + cantAjuste;
        if (nuevoFisico < 0 || (nuevoFisico - this.stockReservado) < 0) {
            throw new IllegalStateException("El ajuste dejaría el stock en negativo.");
        }
        this.stockFisico = nuevoFisico;
    }

    public Long getId() { return id; }
    public Categoria getCategoria() { return categoria; }
    public String getSku() { return sku; }
    public String getCodigoBarras() { return codigoBarras; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    public String getCursoNivel() { return cursoNivel; }
    public String getMateria() { return materia; }
    public Long getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(Long precioVenta) { this.precioVenta = precioVenta; }
    public BigDecimal getMargenUtilidad() { return margenUtilidad; }
    public Long getCpp() { return cpp; }
    public int getStockFisico() { return stockFisico; }
    public int getStockReservado() { return stockReservado; }
    public int getStockCritico() { return stockCritico; }
    public EstadoRegistro getEstado() { return estado; }
}
