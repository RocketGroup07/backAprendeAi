package br.com.aprendeai.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import br.com.aprendeai.model.Usuario;

@Component
public class AuthenticatedUser {
	
	public Usuario getCurrentUser() {        
	    
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
	    if (authentication == null || !(authentication.getPrincipal() instanceof Usuario usuario)) {            
        	throw new RuntimeException("Usuário não autenticado.");        
    	}
	        
        return usuario;    
    }
}
