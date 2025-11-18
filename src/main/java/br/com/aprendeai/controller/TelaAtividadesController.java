package br.com.aprendeai.controller;

import br.com.aprendeai.dtos.AtividadeResponseDto;
import br.com.aprendeai.dtos.AtividadeUpdateDto;
import br.com.aprendeai.model.Arquivo;
import br.com.aprendeai.service.ArquivoService;
import br.com.aprendeai.service.AtividadeService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;                                                                             
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/atividades")
@CrossOrigin
public class TelaAtividadesController {
	private final AtividadeService atividadeService;
	private final ArquivoService arquivoService;

	public TelaAtividadesController(AtividadeService atividadeService, ArquivoService arquivoService) {
		this.atividadeService = atividadeService;
		this.arquivoService = arquivoService;
	}

	@PostMapping(value = "/criar/{turmaId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AtividadeResponseDto> criarAtividade(
            @PathVariable Long turmaId,
           @RequestPart(value = "atividade") String Atividade,
            @RequestPart(value = "arquivo", required = false) MultipartFile arquivo) {
    	
    	return ResponseEntity.ok(atividadeService.criarAtividade(turmaId, Atividade, arquivo));
    }
    
	@GetMapping("/{atividadeId}/download/anexo")
    public ResponseEntity<Resource> baixarArquivoAnexo(@PathVariable Long atividadeId) throws IOException {
		Arquivo arquivo = atividadeService.baixarAnexo(atividadeId);
		
		Resource arquivoAnexo = arquivoService.downloadArquivo(arquivo.getId());
		
		Path caminhoArquivo = Paths.get(arquivo.getCaminhoArquivo());
		var contentType = Files.probeContentType(caminhoArquivo);
		
      return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + arquivo.getNomeArquivo() + "\"")
      		.body(arquivoAnexo);
    }
    
    @GetMapping("/turma/{turmaId}")
    public ResponseEntity<List<AtividadeResponseDto>> listarAtividades(@PathVariable Long turmaId) {
    	return ResponseEntity.ok(atividadeService.listarAtividades(turmaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(atividadeService.buscarPorId(id));
    }

    // Atualizar atividade
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarAtividade(@PathVariable("id") Long id,
                                                @RequestBody AtividadeUpdateDto atividadeAtualizada) {
    	return ResponseEntity.ok(atividadeService.atualizarAtividade(id, atividadeAtualizada));
    }

    // Deletar atividade
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarAtividade(@PathVariable("id") Long id) {
    	atividadeService.deletarAtividade(id);
        return ResponseEntity.noContent().build();
    }
}
