package com.product.app.controller;

import com.product.app.dto.ProductDTO.ProductRequest;
import com.product.app.entity.Product;
import com.product.app.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public Page<Product> list(@RequestParam(name = "name", required = false) String name,
                              @RequestParam(name = "page", defaultValue = "0") Integer page,
                              @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return service.list(name, page, size);
    }

    @GetMapping("/search")
    public Page<Product> byPrice(@RequestParam(name = "min", defaultValue = "0") BigDecimal min,
                                 @RequestParam(name = "max", defaultValue = "9999999") BigDecimal max,
                                 @RequestParam(name = "page", defaultValue = "0") Integer page,
                                 @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return service.byPrice(min, max, page, size);
    }

    @PostMapping
    public ResponseEntity<Product> create(@Valid @RequestBody ProductRequest req) {
        Product p = new Product();
        p.setName(req.name());
        p.setDescription(req.description());
        p.setPrice(req.price());
        return ResponseEntity.ok(service.create(p));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable("id") Long id, @Valid @RequestBody ProductRequest req) {
        Product p = new Product();
        p.setName(req.name());
        p.setDescription(req.description());
        p.setPrice(req.price());
        return ResponseEntity.ok(service.update(id, p));
    }

    @GetMapping("/{id}")
    public Product get(@PathVariable("id") Long id) {
        return service.get(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
