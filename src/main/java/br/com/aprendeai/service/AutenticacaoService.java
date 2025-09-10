package br.com.aprendeai.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import br.com.aprendeai.dtos.AuthDto;

public interface AutenticacaoService extends UserDetailsService{
	
	public String obterToken(AuthDto authDto);
	
	public String validaTokenJwt(String token);

}
