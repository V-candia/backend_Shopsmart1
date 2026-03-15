package com.shopsmart.productos_servicio.controller;

import com.shopsmart.productos_servicio.model.Producto;
import com.shopsmart.productos_servicio.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {
    
    @Autowired
    private ProductoRepository productoRepository;
    
    /**
     * GET /productos
     * Obtiene la lista de todos los productos almacenados en el sistema
     * @return Lista de productos en formato JSON
     */
    @GetMapping
    public ResponseEntity<List<Producto>> obtenerProductos() {
        List<Producto> productos = productoRepository.findAll();
        return ResponseEntity.ok(productos);
    }
    
    /**
     * POST /productos
     * Agrega un nuevo producto al sistema
     * @param producto Objeto Producto con los datos del nuevo producto
     * @return El producto creado con ID generado
     */
    @PostMapping
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto) {
        Producto productoGuardado = productoRepository.save(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productoGuardado);
    }
    
    /**
     * GET /productos/{id}
     * Obtiene un producto específico por su ID
     * @param id ID del producto a obtener
     * @return El producto solicitado
     */
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProductoPorId(@PathVariable Long id) {
        return productoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * PUT /productos/{id}
     * Actualiza un producto existente
     * @param id ID del producto a actualizar
     * @param productoActualizado Datos del producto actualizado
     * @return El producto actualizado
     */
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(
            @PathVariable Long id,
            @RequestBody Producto productoActualizado) {
        return productoRepository.findById(id)
                .map(producto -> {
                    producto.setNombre(productoActualizado.getNombre());
                    producto.setPrecio(productoActualizado.getPrecio());
                    producto.setStock(productoActualizado.getStock());
                    Producto actualizado = productoRepository.save(producto);
                    return ResponseEntity.ok(actualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * DELETE /productos/{id}
     * Elimina un producto del sistema
     * @param id ID del producto a eliminar
     * @return Respuesta sin contenido (204 No Content)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
