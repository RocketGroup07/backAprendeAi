package br.com.aprendeai.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.aprendeai.dtos.RedefinicaoRequestDto;
import br.com.aprendeai.dtos.ValidarCodigoDto;
import br.com.aprendeai.service.RedefinicaoSenha;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/redefinicao")
@CrossOrigin
public class RedefinicaoSenhaController {

    private final RedefinicaoSenha service;

   
    public RedefinicaoSenhaController(RedefinicaoSenha service) {
        this.service = service;
    }

    @PostMapping("/solicitar")
    public String solicitar(@Valid @RequestBody RedefinicaoRequestDto dto) {
        String codigo = gerarCodigo();
//        service.solicitarRedefinicao(dto.email(), codigo);
//        return ResponseEntity.status(HttpStatus.CREATED).build();
        return codigo;
    }

    @PostMapping("/validar")
    public ResponseEntity<?> validar(@Valid @RequestBody ValidarCodigoDto dto) {
        boolean ok = service.validarCodigo(dto.email(), dto.codigo());
        if (ok) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Código inválido ou expirado");
    }

    private String gerarCodigo() {
        StringBuilder sb = new StringBuilder(8);
        String chars = "0123456789";
        java.security.SecureRandom rnd = new java.security.SecureRandom();
        for (int i = 0; i < 8; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
