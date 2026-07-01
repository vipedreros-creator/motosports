package com.motosport.auth.config;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.motosport.auth.controller.AuthController;
import com.motosport.auth.service.AuthService;

// A diferencia de los tests de service/controller, aca SI necesitamos levantar
// el filtro de seguridad real para comprobar el comportamiento de autorizacion.
@WebMvcTest(controllers = AuthController.class)
@Import({ SecurityConfig.class, SecurityConfigTest.TestConfig.class })
class SecurityConfigTest {

	@Autowired
	private MockMvc mockMvc;

	static class TestConfig {
		@Bean
		AuthService authService() {
			return Mockito.mock(AuthService.class);
		}
	}

	@Test
	void login_shouldBeAllowedWithoutAuthentication() throws Exception {
		MvcResult result = mockMvc.perform(post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"email\":\"juan@test.com\",\"password\":\"clave123\"}"))
			.andReturn();

		assertNotEquals(403, result.getResponse().getStatus());
	}

	@Test
	void register_shouldBeAllowedWithoutAuthentication() throws Exception {
		MvcResult result = mockMvc.perform(post("/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"email\":\"nuevo@test.com\",\"password\":\"clave123\"}"))
			.andReturn();

		assertNotEquals(403, result.getResponse().getStatus());
	}

	@Test
	void anyOtherEndpoint_shouldReturnForbiddenWithoutAuthentication() throws Exception {
		mockMvc.perform(get("/perfil"))
			.andExpect(status().isForbidden());
	}
}
