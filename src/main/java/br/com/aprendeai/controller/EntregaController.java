 package br.com.aprendeai.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.aprendeai.dtos.EntregaAtividadeResponseDto;
import br.com.aprendeai.dtos.RequestNotaDto;
import br.com.aprendeai.model.Arquivo;
import br.com.aprendeai.service.ArquivoService;
import br.com.aprendeai.service.EntregaAtividadeService;

@RestController
@RequestMapping("/entregas")
@CrossOrigin
public class EntregaController {
	
	private final EntregaAtividadeService entregaService;
	private final ArquivoService arquivoService;
	
	public EntregaController(EntregaAtividadeService entregaService, ArquivoService arquivoService) {
		this.entregaService = entregaService;
		this.arquivoService = arquivoService;
	}

	@GetMapping("/{atividadeId}/geral")
    public ResponseEntity<List<EntregaAtividadeResponseDto>> listarAtividadesEntregues(@PathVariable Long atividadeId){
    	return ResponseEntity.ok(entregaService.listarEntregasPorAtividadeParaProfessor(atividadeId));
    }
	
	@GetMapping("/{atividadeId}")
	public ResponseEntity<EntregaAtividadeResponseDto> verMinhaEntrega(@PathVariable Long atividadeId){
		return ResponseEntity.ok(entregaService.verMinhaEntrega(atividadeId));
	}
    
    @PostMapping(value = "/{atividadeId}/entregar/", consumes = {"multipart/form-data"})
    public ResponseEntity<?> entregarAtividade(
            @PathVariable("atividadeId") Long atividadeId,
            @RequestPart(value = "resposta", required = false) String resposta,
            @RequestPart(value = "arquivo", required = false) MultipartFile arquivo) {
        return ResponseEntity.ok(entregaService.entregarAtividade(atividadeId, resposta, arquivo));
    }
    
    @GetMapping("/{atividadeId}/entrega/{alunoId}")
    public ResponseEntity<Resource> baixarEntrega(@PathVariable Long atividadeId, @PathVariable Long alunoId) throws IOException{
    	Arquivo arquivo = entregaService.baixarEntregaDeAluno(atividadeId, alunoId);
    	
    	Resource arquivoEntrega = arquivoService.downloadArquivo(arquivo.getId());
    	
    	Path caminhoArquivo = Paths.get(arquivo.getCaminhoArquivo());
		var contentType = Files.probeContentType(caminhoArquivo);
		
		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + arquivo.getNomeArquivo() + "\"")
		      		.body(arquivoEntrega);
    
    }
    
    @PutMapping("/{atividadeId}")
    public ResponseEntity<?> editarEntrega(@PathVariable Long atividadeId,@RequestPart(value = "novaResposta") String novaResposta, 
    		@RequestPart("novoArquivo")  MultipartFile novoArquivo){
    	return ResponseEntity.ok(entregaService.editarEntrega(atividadeId, novaResposta, novoArquivo));
    }
    
    @DeleteMapping("/{atividadeId}")
    public ResponseEntity<?> excluirEntrega(@PathVariable Long atividadeId){
    	entregaService.excluirEntrega(atividadeId);
    	return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{atividadeId}/corrigir/{alunoId}")
    public ResponseEntity<?> corrigirAtividade(@PathVariable("atividadeId") Long atividadeId, @PathVariable("alunoId") Long alunoId, @RequestBody RequestNotaDto dto){
    	return ResponseEntity.ok(entregaService.corrigirAtividade(atividadeId, alunoId, dto));
    }
}
