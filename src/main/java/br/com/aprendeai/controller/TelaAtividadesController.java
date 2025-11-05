package br.com.aprendeai.controller;

import br.com.aprendeai.dtos.AtividadeResponseDto;
import br.com.aprendeai.dtos.AtividadeUpdateDto;
import br.com.aprendeai.dtos.RequestNotaDto;
import br.com.aprendeai.service.AtividadeService;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;                                                                             
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/atividades")
@CrossOrigin
public class TelaAtividadesController {
   private final AtividadeService atividadeService;

    public TelaAtividadesController(AtividadeService atividadeService) {
	super();
	this.atividadeService = atividadeService;
}

	@PostMapping(value = "/criar/{turmaId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AtividadeResponseDto> criarAtividade(
            @PathVariable Long turmaId,
           @RequestPart(value = "atividade") String Atividade,
            @RequestPart(value = "arquivo", required = false) MultipartFile arquivo) {
    	
    	return ResponseEntity.ok(atividadeService.criarAtividade(turmaId, Atividade, arquivo));
    }
    
	@GetMapping("/{atividadeId}/download/anexo")
    public ResponseEntity<Resource> baixarArquivoAnexo(@PathVariable Long atividadeId) {
        return ResponseEntity.ok(atividadeService.baixarAnexo(atividadeId));
    }
    
    @GetMapping("/turma/{turmaId}")
    public ResponseEntity<List<AtividadeResponseDto>> listarAtividades(@PathVariable Long turmaId) {
    	return ResponseEntity.ok(atividadeService.listarAtividades(turmaId));
    }
    
    @GetMapping("/turma/{turmaId}/entregues")
    public ResponseEntity<List<AtividadeResponseDto>> listarAtividadesEntregues(@PathVariable Long turmaId){
    	return ResponseEntity.ok(atividadeService.listarAtividadesEntregues(turmaId));
    }
    
    @PostMapping(value = "/{atividadeId}/entregar/", consumes = {"multipart/form-data"})
    public ResponseEntity<?> entregarAtividade(
            @PathVariable("atividadeId") Long atividadeId,
            @PathVariable("alunoId") Long alunoId,
            @RequestPart(value = "resposta") String resposta,
            @RequestPart("arquivo") MultipartFile arquivo) {
        return ResponseEntity.ok(atividadeService.entregarAtividade(atividadeId, resposta, arquivo));
    }
    
    @PostMapping("/{atividadeId}/entrega")
    public ResponseEntity<?> editarEntrega(@PathVariable Long atividadeId,@RequestPart(value = "novaResposta") String novaResposta, 
    		@RequestPart("novoArquivo")  MultipartFile novoArquivo){
    	return ResponseEntity.ok(atividadeService.editarEntrega(atividadeId, novaResposta, novoArquivo));
    }
    
    @DeleteMapping("/{atividadeId}/entrega")
    public ResponseEntity<?> excluirEntrega(@PathVariable Long atividadeId){
    	atividadeService.excluirEntrega(atividadeId);
    	return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(atividadeService.buscarPorId(id));
    }
    
    @PutMapping("/{atividadeId}/corrigir")
    public ResponseEntity<?> corrigirAtividade(@PathVariable("atividadeId") Long atividadeId, @RequestBody RequestNotaDto dto){
    	return ResponseEntity.ok(atividadeService.corrigirAtividade(atividadeId, dto));
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
