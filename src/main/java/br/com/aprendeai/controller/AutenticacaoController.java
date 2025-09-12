package br.com.aprendeai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.aprendeai.dtos.AuthDto;
import br.com.aprendeai.service.AutenticacaoService;

@RestController
@RequestMapping("/login")
@CrossOrigin
public class AutenticacaoController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private AutenticacaoService autenticacaoService;
	
	@PostMapping("/")
	@ResponseStatus(HttpStatus.OK)
    public String login(@RequestBody AuthDto authDto) {
        try {
        	var usuarioAutenticationToken = new UsernamePasswordAuthenticationToken(authDto.login(), authDto.senha());
    		
    		authenticationManager.authenticate(usuarioAutenticationToken);
    		
    		return autenticacaoService.obterToken(authDto);
        } catch (Exception e) {
            return "Ocorreu um erro ao realizar login";
        }
    }
}