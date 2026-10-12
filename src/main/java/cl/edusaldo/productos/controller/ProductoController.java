package cl.edusaldo.productos.controller;

import cl.edusaldo.productos.dto.CrearProductoDTO;
import cl.edusaldo.productos.dto.ProductoResponseDTO;
import cl.edusaldo.productos.model.Producto;
import cl.edusaldo.productos.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "Catálogo escolar, precios y maestros de artículos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @PostMapping
    @Operation(summary = "Crear nuevo producto en el catálogo")
    public ResponseEntity<ProductoResponseDTO> crear(@Valid @RequestBody CrearProductoDTO dto) {
        Producto p = productoService.crearProducto(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ProductoResponseDTO(p));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar producto y stock disponible por ID")
    public ResponseEntity<ProductoResponseDTO> obtenerPorId(@PathVariable Long id) {
        Producto p = productoService.obtenerPorId(id);
        return ResponseEntity.ok(new ProductoResponseDTO(p));
    }

    @GetMapping
    @Operation(summary = "Listar catálogo completo de productos")
    public ResponseEntity<List<ProductoResponseDTO>> listarTodos() {
        List<ProductoResponseDTO> list = productoService.listarTodos().stream()
                .map(ProductoResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }
}
