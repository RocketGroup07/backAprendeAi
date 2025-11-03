//package br.com.aprendeai.controller;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import br.com.aprendeai.dtos.RedefinicaoRequestDto;
//import br.com.aprendeai.dtos.ValidarCodigoDto;
//import br.com.aprendeai.service.RedefinicaoSenha;
//
//import jakarta.validation.Valid;
//
//@RestController
//@RequestMapping("/redefinicao")
//@CrossOrigin
//public class RedefinicaoSenhaController {
//
//    private final RedefinicaoSenha service;
//
//    public RedefinicaoSenhaController(RedefinicaoSenha service) {
//        this.service = service;
//    }
//
//    @PostMapping("/solicitar")
//    public ResponseEntity<?> solicitar(@Valid @RequestBody RedefinicaoRequestDto dto) {
//        service.solicitarRedefinicao(dto.email());
//        return ResponseEntity.ok("Código enviado para o e-mail.");
//    }
//
//    @PostMapping("/validar")
//    public ResponseEntity<?> validar(@Valid @RequestBody ValidarCodigoDto dto) {
//        boolean ok = service.validarCodigo(dto.email(), dto.codigo());
//        if (ok) return ResponseEntity.ok("Código válido!");
//        return ResponseEntity.badRequest().body("Código inválido ou expirado.");
//    }
//}
