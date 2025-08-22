package com.product.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.app.config.JwtAuthFilter;
import com.product.app.controller.ProductController;
import com.product.app.dto.ProductDTO.ProductRequest;
import com.product.app.entity.Product;
import com.product.app.exception.ApiExceptionHandler;
import com.product.app.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.annotation.Resource;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(ApiExceptionHandler.class)
class ProductControllerWebMvcTest {

    @Resource
    MockMvc mvc;
    @Resource
    ObjectMapper om;

    @MockBean
    ProductService productService;
    @MockBean
    JwtAuthFilter jwtAuthFilter;

    private Product makeProduct(Long id, String name, double price) {
        Product p = new Product();
        p.setId(id);
        p.setName(name);
        p.setDescription(name + " desc");
        p.setPrice(BigDecimal.valueOf(price));
        p.setCreatedAt(Instant.now());
        return p;
    }

    @Test
    @DisplayName("GET /api/products (list) with and without name")
    void list_ok() throws Exception {
        var page = new PageImpl<>(List.of(makeProduct(1L, "Pen", 1.5)),
                PageRequest.of(0, 10), 1);

        Mockito.when(productService.list(eq("Pen"), anyInt(), anyInt(), eq(Boolean.FALSE)))
                .thenReturn(page);

        // with name
        mvc.perform(get("/api/products")
                        .param("name", "Pen")
                        .param("page", "0")
                        .param("size", "10")
                        .param("ordered", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Pen"));

        // without name (also cover the other branch)
        Mockito.when(productService.list(isNull(), anyInt(), anyInt(), eq(Boolean.FALSE)))
                .thenReturn(page);

        mvc.perform(get("/api/products")
                        .param("page", "0")
                        .param("size", "10")
                        .param("ordered", "false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1));
    }

    @Test
    @DisplayName("GET /api/products/search by price range")
    void search_ok() throws Exception {
        var page = new PageImpl<>(List.of(makeProduct(2L, "Notebook", 3.99)),
                PageRequest.of(0, 10), 1);
        Mockito.when(productService.byPrice(eq(BigDecimal.valueOf(1)),
                        eq(BigDecimal.valueOf(10)),
                        anyInt(), anyInt(), eq(Boolean.FALSE)))
                .thenReturn(page);

        mvc.perform(get("/api/products/search")
                        .param("min", "1")
                        .param("max", "10")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Notebook"));
    }

    @Test
    @DisplayName("POST /api/products create")
    void create_ok() throws Exception {
        Product created = makeProduct(10L, "Marker", 2.5);
        Mockito.when(productService.create(any(Product.class))).thenReturn(created);

        var body = om.writeValueAsString(new ProductRequest("Marker", "Permanent", BigDecimal.valueOf(2.5)));

        mvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("Marker"));
    }

    @Test
    @DisplayName("PUT /api/products/{id} update")
    void update_ok() throws Exception {
        Product updated = makeProduct(10L, "Marker", 2.75);
        Mockito.when(productService.update(eq(10L), any(Product.class))).thenReturn(updated);

        var body = om.writeValueAsString(new ProductRequest("Marker", "Red", BigDecimal.valueOf(2.75)));

        mvc.perform(put("/api/products/{id}", 10)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(2.75));
    }

    @Test
    @DisplayName("GET /api/products/{id} get by id")
    void get_ok() throws Exception {
        Mockito.when(productService.get(5L)).thenReturn(makeProduct(5L, "Mug", 5.49));

        mvc.perform(get("/api/products/{id}", 5))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mug"));
    }

    @Test
    @DisplayName("DELETE /api/products/{id} -> 204")
    void delete_ok() throws Exception {
        mvc.perform(delete("/api/products/{id}", 9))
                .andExpect(status().isNoContent());
        Mockito.verify(productService).delete(9L);
    }
}
