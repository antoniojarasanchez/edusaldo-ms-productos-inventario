package cl.edusaldo.productos.dto;

import cl.edusaldo.productos.model.EstadoRegistro;
import cl.edusaldo.productos.model.Producto;

public class ProductoResponseDTO {
    private Long id;
    private String sku;
    private String codigoBarras;
    private String nombre;
    private String descripcion;
    private String imagenUrl;
    private String cursoNivel;
    private String materia;
    private Long precioVenta;
    private Long cpp;
    private int stockFisico;
    private int stockReservado;
    private int stockDisponible;
    private int stockCritico;
    private boolean esCritico;
    private String categoria;
    private EstadoRegistro estado;

    public ProductoResponseDTO() {}

    public ProductoResponseDTO(Producto p) {
        this.id = p.getId();
        this.sku = p.getSku();
        this.codigoBarras = p.getCodigoBarras();
        this.nombre = p.getNombre();
        this.descripcion = p.getDescripcion();
        this.imagenUrl = p.getImagenUrl();
        this.cursoNivel = p.getCursoNivel();
        this.materia = p.getMateria();
        this.precioVenta = p.getPrecioVenta();
        this.cpp = p.getCpp();
        this.stockFisico = p.getStockFisico();
        this.stockReservado = p.getStockReservado();
        this.stockDisponible = p.getStockDisponible();
        this.stockCritico = p.getStockCritico();
        this.esCritico = p.esCritico();
        this.categoria = p.getCategoria() != null ? p.getCategoria().getNombre() : "Sin Categoría";
        this.estado = p.getEstado();
    }

    public Long getId() { return id; }
    public String getSku() { return sku; }
    public String getCodigoBarras() { return codigoBarras; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getImagenUrl() { return imagenUrl; }
    public String getCursoNivel() { return cursoNivel; }
    public String getMateria() { return materia; }
    public Long getPrecioVenta() { return precioVenta; }
    public Long getCpp() { return cpp; }
    public int getStockFisico() { return stockFisico; }
    public int getStockReservado() { return stockReservado; }
    public int getStockDisponible() { return stockDisponible; }
    public int getStockCritico() { return stockCritico; }
    public boolean isEsCritico() { return esCritico; }
    public String getCategoria() { return categoria; }
    public EstadoRegistro getEstado() { return estado; }
}
