package cl.edusaldo.productos.service;

import cl.edusaldo.productos.dto.CrearProductoDTO;
import cl.edusaldo.productos.model.Categoria;
import cl.edusaldo.productos.model.Producto;
import cl.edusaldo.productos.repository.CategoriaRepository;
import cl.edusaldo.productos.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional
    public Producto crearProducto(CrearProductoDTO dto) {
        if (productoRepository.existsBySku(dto.getSku())) {
            throw new IllegalArgumentException("Ya existe un producto con el SKU: " + dto.getSku());
        }

        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada con ID: " + dto.getCategoriaId()));

        Producto p = new Producto(
                categoria, dto.getSku(), dto.getCodigoBarras(), dto.getNombre(),
                dto.getCursoNivel(), dto.getMateria(), dto.getPrecioVenta(),
                dto.getCpp(), dto.getStockInicial(), dto.getStockCritico()
        );
        p.setDescripcion(dto.getDescripcion());
        p.setImagenUrl(dto.getImagenUrl());

        return productoRepository.save(p);
    }

    @Transactional(readOnly = true)
    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }
}
