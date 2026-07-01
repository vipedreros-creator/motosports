package com.motosport.auth.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.motosport.auth.domain.User;

@SpringBootTest
// Perfil test usa H2 en memoria para aislar la DB real de desarrollo.
@ActiveProfiles("test")
class UserRepositoryTest {

	// En test de integracion, Spring inyecta el bean real del repository.
	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	@SuppressWarnings("unused")
	void setUp() {
		// Nota: las migraciones de Flyway del proyecto usan pgcrypto (especifico de Postgres),
		// por lo que no corren sobre H2. Sembramos los datos directamente vía el repository.
		userRepository.deleteAll();
		userRepository.save(new User("link@hyrule.com", "hashDeClave"));

		User inactivo = new User("ganondorf@hyrule.com", "hashDeClave");
		inactivo.setActive(false);
		userRepository.save(inactivo);
	}

	@Test
	void findByEmailAndActiveTrue_shouldFindActiveUser() {
		var result = userRepository.findByEmailAndActiveTrue("link@hyrule.com");

		assertTrue(result.isPresent());
		assertEquals("link@hyrule.com", result.get().getEmail());
	}

	@Test
	void findByEmailAndActiveTrue_shouldNotFindInactiveUser() {
		var result = userRepository.findByEmailAndActiveTrue("ganondorf@hyrule.com");

		assertFalse(result.isPresent());
	}

	@Test
	void findByEmailAndActiveTrue_shouldNotFindUnknownEmail() {
		var result = userRepository.findByEmailAndActiveTrue("no-existe@hyrule.com");

		assertFalse(result.isPresent());
	}

	@Test
	void existsById_shouldReturnTrueWhenEmailIsRegistered() {
		assertTrue(userRepository.existsById("link@hyrule.com"));
	}

	@Test
	void existsById_shouldReturnFalseWhenEmailIsNotRegistered() {
		assertFalse(userRepository.existsById("no-existe@hyrule.com"));
	}
}
