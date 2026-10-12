package cl.edusaldo.productos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CrearProductoDTO {

    @NotNull(message = "El ID de categoría es obligatorio")
    private Long categoriaId;

    @NotBlank(message = "El SKU es obligatorio")
    private String sku;

    @NotBlank(message = "El código de barras es obligatorio")
    private String codigoBarras;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String descripcion;
    private String imagenUrl;

    @NotBlank(message = "El curso/nivel es obligatorio")
    private String cursoNivel;

    @NotBlank(message = "La materia es obligatoria")
    private String materia;

    @NotNull(message = "El precio de venta es obligatorio")
    @Positive(message = "El precio debe ser positivo")
    private Long precioVenta;

    @NotNull(message = "El CPP inicial es obligatorio")
    @Positive(message = "El CPP debe ser positivo")
    private Long cpp;

    @Min(value = 0, message = "El stock inicial no puede ser negativo")
    private int stockInicial = 0;

    @Min(value = 1, message = "El stock crítico debe ser al menos 1")
    private int stockCritico = 5;

    public CrearProductoDTO() {}

    public Long getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Long categoriaId) { this.categoriaId = categoriaId; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public String getCodigoBarras() { return codigoBarras; }
    public void setCodigoBarras(String codigoBarras) { this.codigoBarras = codigoBarras; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    public String getCursoNivel() { return cursoNivel; }
    public void setCursoNivel(String cursoNivel) { this.cursoNivel = cursoNivel; }
    public String getMateria() { return materia; }
    public void setMateria(String materia) { this.materia = materia; }
    public Long getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(Long precioVenta) { this.precioVenta = precioVenta; }
    public Long getCpp() { return cpp; }
    public void setCpp(Long cpp) { this.cpp = cpp; }
    public int getStockInicial() { return stockInicial; }
    public void setStockInicial(int stockInicial) { this.stockInicial = stockInicial; }
    public int getStockCritico() { return stockCritico; }
    public void setStockCritico(int stockCritico) { this.stockCritico = stockCritico; }
}
