package br.com.aprendeai.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.aprendeai.dtos.AuthDto;
import br.com.aprendeai.dtos.LoginResponseDto;
import br.com.aprendeai.service.AutenticacaoService;

@RestController
@RequestMapping("/login")
@CrossOrigin
public class AutenticacaoController {
	
	private final AutenticacaoService autenticacaoService;
	
	public AutenticacaoController(AutenticacaoService autenticacaoService) {
		this.autenticacaoService = autenticacaoService;
	}

	@PostMapping("/")
	@ResponseStatus(HttpStatus.OK)
	 public ResponseEntity<LoginResponseDto> login(@RequestBody AuthDto authDto) {
        try {
        	LoginResponseDto response = autenticacaoService.autenticarELogar(authDto);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            LoginResponseDto errorResponse = new LoginResponseDto(null, null, "Login ou senha incorretos.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        } catch (Exception e) {
            LoginResponseDto errorResponse = new LoginResponseDto(null, null, "Ocorreu um erro ao realizar o login.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}