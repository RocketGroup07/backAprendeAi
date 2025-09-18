package br.com.aprendeai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.aprendeai.dtos.AuthDto;
import br.com.aprendeai.dtos.LoginResponseDto;
import br.com.aprendeai.dtos.UsuarioResponseDto;
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
	 public ResponseEntity<LoginResponseDto> login(@RequestBody AuthDto authDto) {
        try {
//        	var usuarioAutenticationToken = new UsernamePasswordAuthenticationToken(authDto.login(), authDto.senha());
//    		
//    		authenticationManager.authenticate(usuarioAutenticationToken);
//    		
//    		return autenticacaoService.obterToken(authDto);
        	
        	LoginResponseDto response = autenticacaoService.autenticarELogar(authDto);
            return ResponseEntity.ok(response);
            
        } catch (BadCredentialsException e) {
            // Em caso de credenciais inválidas, você pode retornar um status de não autorizado
            LoginResponseDto errorResponse = new LoginResponseDto(null, null, "Login ou senha incorretos.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        } catch (Exception e) {
            // Para outros erros (ex: erro na geração do token)
            LoginResponseDto errorResponse = new LoginResponseDto(null, null, "Ocorreu um erro ao realizar o login.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}