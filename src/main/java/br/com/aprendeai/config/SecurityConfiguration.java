package br.com.aprendeai.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	
	@Autowired
	private SecurityFilter securityFilter;
	
	private static final String[] USER_URLS = {
			"/alunos/",
			"/posts/",
			"/favoritos/"
	        
	};
	
	private static final String[] PERMIT_URLS = {
			"/login/",
			"/alunos/cadastro-com-turma",
			"/alunos/cadastrar",
			"/api/arquivos/**",
			"/swagger-ui/index.html",
	        "/swagger-ui/**",
	        "/v3/api-docs/**",
	        "/swagger-resources/**",
	        "/webjars/**",
	        "/settings/**",
	        "/atividades"
	};
	
	private static final String[] ADMIN_URLS = {
			"/professores/",
			"/turmas/"
	};
	
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
		return httpSecurity
				.csrf(AbstractHttpConfigurer::disable)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(authoriza -> authoriza
                        .requestMatchers(PERMIT_URLS).permitAll()
                        .requestMatchers(USER_URLS).hasRole("USER")
                        .requestMatchers(ADMIN_URLS).hasRole("USER")			      
                        .anyRequest().authenticated()
				)
				.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
}
