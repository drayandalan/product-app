package com.product.app.dto;

import java.math.BigDecimal;

public class ProductDTO {
    public record ProductRequest(String name, String description, BigDecimal price) {
    }
}
