package com.product.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.app.config.JwtAuthFilter;
import com.product.app.controller.AuthController;
import com.product.app.dto.AuthDTO.*;
import com.product.app.entity.UserAccount;
import com.product.app.service.JwtService;
import com.product.app.service.UserService;
import com.product.app.exception.ApiExceptionHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.annotation.Resource;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false) // skip security filters; we test controller only
@Import(ApiExceptionHandler.class)
class AuthControllerWebMvcTest {

    @Resource
    MockMvc mvc;
    @Resource
    ObjectMapper om;

    @MockBean
    UserService userService;
    @MockBean
    JwtService jwtService;
    @MockBean
    JwtAuthFilter jwtAuthFilter;

    @Test
    void register_ok() throws Exception {
        var u = new UserAccount();
        u.setUsername("john");
        u.setRole("USER");
        Mockito.when(userService.register("john", "secret")).thenReturn(u);
        Mockito.when(jwtService.createToken("john", "USER")).thenReturn("abc.jwt");

        var body = om.writeValueAsString(new RegisterRequest("john", "secret"));

        mvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("abc.jwt"));
    }

    @Test
    void login_ok() throws Exception {
        var u = new UserAccount();
        u.setUsername("john");
        u.setRole("USER");
        Mockito.when(userService.find("john")).thenReturn(Optional.of(u));
        Mockito.when(userService.matches(eq("secret"), any())).thenReturn(true);
        Mockito.when(jwtService.createToken("john", "USER")).thenReturn("xyz.jwt");

        var body = om.writeValueAsString(new LoginRequest("john", "secret"));

        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("xyz.jwt"));
    }

    @Test
    @DisplayName("POST /api/auth/login -> 400 when bad credentials")
    void login_badCredentials() throws Exception {
        Mockito.when(userService.find("nope")).thenReturn(Optional.empty());

        var body = om.writeValueAsString(new LoginRequest("nope", "wrong"));

        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists());
    }
}
