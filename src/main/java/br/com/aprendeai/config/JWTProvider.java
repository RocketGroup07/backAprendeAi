package br.com.aprendeai.config;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

@Service
public class JWTProvider {
	
	public String validaTokenJwt(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256("my-secret");
			
			return JWT.require(algorithm)
					.withIssuer("auth-api")
					.build()
					.verify(token)
					.getSubject();
		} catch (JWTVerificationException exception) {
			System.out.println("Token inválido ou expirado");
			return null;
		}
	}

}
