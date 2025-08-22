package com.product.app.service;

import com.product.app.entity.Product;
import com.product.app.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    @Cacheable(
            cacheNames = "products",
            key = "'list|p=' + #a1 + '|s=' + #a2 + '|o=' + #a3 + '|n=' + (#a0 == null ? '' : #a0.trim().toLowerCase())"
    )
    public Page<Product> list(String name, Integer page, Integer size, Boolean ordered) {
        PageRequest pr = PageRequest.of(page, size);
        return (name == null || name.isBlank())
                ? ordered ? repo.findAllByOrderByPriceDesc(pr) : repo.findAll(pr)
                : repo.findByNameContainingIgnoreCase(name.trim(), pr);
    }

    @Cacheable(
            cacheNames = "products",
            key = "'price|p=' + #a2 + '|s=' + #a3 + '|o=' + #a4 + '|min=' + #a0 + '|max=' + #a1"
    )
    public Page<Product> byPrice(BigDecimal min, BigDecimal max, Integer page, Integer size, Boolean ordered) {
        PageRequest pr = PageRequest.of(page, size);
        return ordered ? repo.findByPriceBetweenOrderByPriceDesc(min, max, pr) : repo.findByPriceBetween(min, max, pr);
    }


    @CacheEvict(value = "products", allEntries = true)
    public Product create(Product p) {
        return repo.save(p);
    }

    @CacheEvict(value = "products", allEntries = true)
    public Product update(Long id, Product in) {
        Product p = repo.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        p.setName(in.getName());
        p.setDescription(in.getDescription());
        p.setPrice(in.getPrice());
        return repo.save(p);
    }

    public Product get(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @CacheEvict(value = "products", allEntries = true)
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
