package com.motosport.auth.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.motosport.auth.dto.AuthResponse;
import com.motosport.auth.dto.LoginRequest;
import com.motosport.auth.dto.RegisterRequest;
import com.motosport.auth.service.AuthService;

// Integra Mockito con JUnit 5.
// Aisla la capa web mockeando AuthService: no se levanta contexto de Spring.
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

	@Mock
	private AuthService authService;

	@Test
	void login_shouldReturnAuthResponseFromService() {
		AuthController authController = new AuthController(authService);
		when(authService.login(any())).thenReturn(new AuthResponse("token-123"));

		AuthResponse result = authController.login(new LoginRequest("juan@test.com", "clave123"));

		assertEquals("token-123", result.token());
	}

	@Test
	void register_shouldReturnAuthResponseFromService() {
		AuthController authController = new AuthController(authService);
		when(authService.register(any())).thenReturn(new AuthResponse("token-456"));

		AuthResponse result = authController.register(new RegisterRequest("nuevo@test.com", "clave123"));

		assertEquals("token-456", result.token());
	}
}
