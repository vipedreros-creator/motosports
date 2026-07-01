package com.motosport.auth.service;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

class JwtServiceTest {

	private static final String SECRET = "test-secret-key-not-for-production-use-only-testing";

	// Test puro: sin mocks, sin contexto de Spring, ideal para feedback rapido.
	private JwtService jwtService;

	@BeforeEach
	@SuppressWarnings("unused")
	void setUp() {
		jwtService = new JwtService(SECRET, 1L);
	}

	@Test
	void generateToken_shouldContainEmailAsSubject() {
		String token = jwtService.generateToken("juan@test.com");

		Claims claims = parseClaims(token);

		assertEquals("juan@test.com", claims.getSubject());
	}

	@Test
	void generateToken_shouldSetExpirationInTheFuture() {
		String token = jwtService.generateToken("juan@test.com");

		Claims claims = parseClaims(token);

		assertTrue(claims.getExpiration().after(new Date()));
		assertTrue(claims.getExpiration().after(claims.getIssuedAt()));
	}

	@Test
	void generateToken_shouldReturnDifferentTokensForDifferentEmails() {
		String tokenJuan = jwtService.generateToken("juan@test.com");
		String tokenMaria = jwtService.generateToken("maria@test.com");

		assertEquals("juan@test.com", parseClaims(tokenJuan).getSubject());
		assertEquals("maria@test.com", parseClaims(tokenMaria).getSubject());
	}

	private Claims parseClaims(String token) {
		var signingKey = Keys.hmacShaKeyFor(sha256(SECRET));
		return Jwts.parser()
			.verifyWith(signingKey)
			.build()
			.parseSignedClaims(token)
			.getPayload();
	}

	private byte[] sha256(String value) {
		try {
			return java.security.MessageDigest.getInstance("SHA-256")
				.digest(value.getBytes(java.nio.charset.StandardCharsets.UTF_8));
		} catch (java.security.NoSuchAlgorithmException exception) {
			throw new IllegalStateException(exception);
		}
	}
}
