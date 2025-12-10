package br.com.aprendeai.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.aprendeai.model.Usuario;
import br.com.aprendeai.repository.UsuarioRepository;
import br.com.aprendeai.service.AutenticacaoService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter{

	private final JWTProvider jwt;
	
	private final UsuarioRepository usuarioRepository;
	
	public SecurityFilter(JWTProvider jwt, UsuarioRepository usuarioRepository) {
		this.jwt = jwt;
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String token = extraiTokenHeader(request);
		
		if(token != null) {
				String login = jwt.validaTokenJwt(token);
				
				Usuario usuario = usuarioRepository.findByLogin(login);
				
				var autentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
				
				SecurityContextHolder.getContext().setAuthentication(autentication);
		}
		
		filterChain.doFilter(request, response);
	}
	
	public String extraiTokenHeader(HttpServletRequest request) {
		var authHeader = request.getHeader("Authorization");
		
		if(authHeader == null) {
			return null;
		}
		
		if(!authHeader.split(" ")[0].equals("Bearer")) {
			return null;
		}
		
		return authHeader.split(" ")[1];
	}
}