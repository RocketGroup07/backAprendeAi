package br.com.aprendeai.service.impl;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import br.com.aprendeai.dtos.AuthDto;
import br.com.aprendeai.model.Usuario;
import br.com.aprendeai.repository.UsuarioRepository;
import br.com.aprendeai.service.AutenticacaoService;

@Service
public class AutenticacaoServiceImpl implements AutenticacaoService{

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		return usuarioRepository.findByLogin(login);
	}
	
	@Override
	public String obterToken(AuthDto authDto) {
		
		Usuario usuario = usuarioRepository.findByLogin(authDto.login());
		
		return gerarTokenJwt(usuario);
	}
	
	public String gerarTokenJwt(Usuario usuario) {
		
		try {
			
			Algorithm algorithm = Algorithm.HMAC256("my-secret");
			
			return JWT.create()
					.withIssuer("auth-api")
					.withSubject(usuario.getLogin())
					.withExpiresAt(gerarDataExpiracao())
					.sign(algorithm);
		} catch (JWTCreationException exception) {
			throw new RuntimeException("Erro ao tentar gerar o token!" + exception.getMessage());
		}
		
	}
	
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

	private Instant gerarDataExpiracao() {
		return LocalDateTime.now()
				.plusHours(8)
				.toInstant(ZoneOffset.of("-03:00"));
	}
}
