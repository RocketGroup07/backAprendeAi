package br.com.aprendeai.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import br.com.aprendeai.model.Arquivo;
import br.com.aprendeai.service.ArquivoService;

import java.util.List;

@RestController
@RequestMapping("/api/arquivos")
public class ArquivoController {

    private final ArquivoService arquivoService;

    public ArquivoController(ArquivoService arquivoService) {
        this.arquivoService = arquivoService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadArquivo(@RequestParam("arquivo") MultipartFile arquivo) {
        try {
            Arquivo novoArquivo = arquivoService.uploadArquivo(arquivo);
            String urlDownload = "/api/arquivos/download/" + novoArquivo.getId();
            return ResponseEntity.status(HttpStatus.CREATED).body("Arquivo enviado com sucesso. ID: " + novoArquivo.getId() + ". URL para download: " + urlDownload);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Falha ao enviar o arquivo: " + e.getMessage());
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadArquivo(@PathVariable Long id) {
        try {
            Arquivo arquivo = arquivoService.getArquivo(id);
            Resource recurso = arquivoService.downloadArquivo(id);
    
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + arquivo.getNomeArquivo() + "\"");
            headers.add(HttpHeaders.CONTENT_TYPE, arquivo.getTipoArquivo());
    
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(recurso);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping ("/")
    public ResponseEntity<List<Arquivo>> listarArquivos() {
        List<Arquivo> arquivos = arquivoService.listarArquivos();
        return ResponseEntity.ok(arquivos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarArquivo(@PathVariable Long id) {
        try {
            arquivoService.deletarArquivo(id);
            return ResponseEntity.ok("Arquivo deletado com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Falha ao deletar o arquivo: " + e.getMessage());
        }
    }
}