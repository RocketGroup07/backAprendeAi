package br.com.aprendeai.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
//@EnableMethodSecurity
public class SecurityConfiguration {
	
	private final SecurityFilter securityFilter;
	private final CustomPermissionEvaluator permissionEvaluator;
	
	public SecurityConfiguration(SecurityFilter securityFilter, CustomPermissionEvaluator permissionEvaluator) {
		this.securityFilter = securityFilter;
		this.permissionEvaluator = permissionEvaluator;
	}

	private static final String[] USER_URLS = {
			"/alunos/**",
			"/posts/**",
			"/favoritos/**",
			"/atividades/**"
	};
	
	private static final String[] PERMIT_URLS = {
			"/login/",
			"/alunos/cadastro-com-turma",
			"/alunos/cadastrar",
			"/api/arquivos/**",
	        "/turmas/validar-codigo",
	        "/swagger-ui.html", 
	        "/swagger-ui/**",
	        "/v3/api-docs/**",
	        "/api/**"
	};
	
	private static final String[] ADMIN_URLS = {
			"/professores/**",
			"/turmas/**"
	};
	
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
		return httpSecurity
				.csrf(csrf -> csrf.disable())
		        .cors(cors -> cors.configurationSource(request -> {
		            CorsConfiguration config = new CorsConfiguration();
		            config.setAllowedOrigins(List.of("*")); 
		            config.setAllowedMethods(List.of("*"));
		            config.setAllowedHeaders(List.of("*"));
		            return config;
		        })).csrf(csrf -> csrf.disable())
//				.csrf(AbstractHttpConfigurer::disable)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(authoriza -> authoriza
						.requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
						
                        .requestMatchers(PERMIT_URLS).permitAll()
                        .requestMatchers(USER_URLS).hasRole("USER")
                        .requestMatchers(ADMIN_URLS).hasRole("ADMIN")			      
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
	
//	@Bean
//    public MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
//        DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
//        handler.setPermissionEvaluator(permissionEvaluator);
//        return handler;
//    }
	
}
