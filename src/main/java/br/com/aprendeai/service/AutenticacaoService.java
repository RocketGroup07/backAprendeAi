package br.com.aprendeai.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import br.com.aprendeai.dtos.AuthDto;
import br.com.aprendeai.dtos.LoginResponseDto;

public interface AutenticacaoService extends UserDetailsService{
	
	public String obterToken(AuthDto authDto);
	
	public String validaTokenJwt(String token);
	
	public LoginResponseDto autenticarELogar(AuthDto authDto);

}
