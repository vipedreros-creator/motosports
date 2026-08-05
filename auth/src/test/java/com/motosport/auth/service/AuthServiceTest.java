package com.motosport.auth.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import com.motosport.auth.domain.User;
import com.motosport.auth.dto.AuthResponse;
import com.motosport.auth.dto.LoginRequest;
import com.motosport.auth.dto.RegisterRequest;
import com.motosport.auth.repository.UserRepository;

// Integra Mockito con JUnit 5.
// Gracias a esta anotacion, los campos con @Mock se crean automaticamente antes de cada test.
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	// Mock: reemplaza las dependencias reales por dobles de prueba controlables.
	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private JwtService jwtService;

	@Test
	void login_shouldReturnTokenWhenCredentialsAreValid() {
		AuthService authService = new AuthService(userRepository, passwordEncoder, jwtService);
		User user = new User("juan@test.com", "hashClave");
		when(userRepository.findByEmailAndActiveTrue("juan@test.com")).thenReturn(Optional.of(user));
		when(passwordEncoder.matches("clave123", "hashClave")).thenReturn(true);
		when(jwtService.generateToken("juan@test.com")).thenReturn("token-generado");

		AuthResponse result = authService.login(new LoginRequest("juan@test.com", "clave123"));

		assertEquals("token-generado", result.token());
	}

	@Test
	void login_shouldReturnUnauthorizedWhenUserMissing() {
		AuthService authService = new AuthService(userRepository, passwordEncoder, jwtService);
		when(userRepository.findByEmailAndActiveTrue("nadie@test.com")).thenReturn(Optional.empty());

		LoginRequest request = new LoginRequest("nadie@test.com", "clave123");
		ResponseStatusException result = assertThrows(
				ResponseStatusException.class,
				() -> authService.login(request));

		assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());

		verify(jwtService, never()).generateToken(anyString());
	}

	@Test
	void login_shouldReturnUnauthorizedWhenPasswordDoesNotMatch() {
		AuthService authService = new AuthService(userRepository, passwordEncoder, jwtService);
		User user = new User("juan@test.com", "hashClave");
		when(userRepository.findByEmailAndActiveTrue("juan@test.com")).thenReturn(Optional.of(user));
		when(passwordEncoder.matches("claveMala", "hashClave")).thenReturn(false);

		LoginRequest request = new LoginRequest("juan@test.com", "claveMala");
		ResponseStatusException result = assertThrows(
				ResponseStatusException.class,
				() -> authService.login(request));

		assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());

		verify(jwtService, never()).generateToken(anyString());
	}

	@Test
	void register_shouldReturnConflictWhenEmailAlreadyExists() {
		AuthService authService = new AuthService(userRepository, passwordEncoder, jwtService);
		when(userRepository.existsById("existente@test.com")).thenReturn(true);

		RegisterRequest request = new RegisterRequest("existente@test.com", "clave123");
		ResponseStatusException result = assertThrows(
				ResponseStatusException.class,
				() -> authService.register(request));

		assertEquals(HttpStatus.CONFLICT, result.getStatusCode());

		verify(userRepository, never()).save(any());
	}

	@Test
	void register_shouldSaveAndReturnToken() {
		AuthService authService = new AuthService(userRepository, passwordEncoder, jwtService);
		when(userRepository.existsById("nuevo@test.com")).thenReturn(false);
		when(passwordEncoder.encode("clave123")).thenReturn("claveEncriptada");
		when(jwtService.generateToken("nuevo@test.com")).thenReturn("token-nuevo");

		AuthResponse result = authService.register(new RegisterRequest("nuevo@test.com", "clave123"));

		assertEquals("token-nuevo", result.token());

		// Captor permite inspeccionar el argumento real enviado al mock.
		ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
		verify(userRepository).save(captor.capture());
		assertEquals("nuevo@test.com", captor.getValue().getEmail());
		assertEquals("claveEncriptada", captor.getValue().getPassword());
	}
}
