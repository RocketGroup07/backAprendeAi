package br.com.aprendeai.controller;

import br.com.aprendeai.dtos.*;
import br.com.aprendeai.model.Arquivo;
import br.com.aprendeai.service.ArquivoService;
import br.com.aprendeai.service.ChamadaService;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/chamada")
@CrossOrigin
public class ChamadaController {

    private final ChamadaService chamadaService;
    private final ArquivoService arquivoService;

    public ChamadaController(ChamadaService chamadaService, ArquivoService arquivoService) {
		this.chamadaService = chamadaService;
		this.arquivoService = arquivoService;
	}

	/**
     * Inicializa a chamada do dia.
     * Pré-preenche todas as presenças com as horas máximas do dia.
     * POST /api/chamada/inicializar
     */
    @PostMapping("/inicializar")
    public List<PresencaDTO> inicializarChamada(@RequestBody InicializarChamadaDTO dto) {
        return chamadaService.inicializarChamada(dto);
    }

    /**
     * Atualiza a presença individual de um aluno.
     * PATCH /api/chamada/presenca/{id}?horasPresente=X
     */
    @PatchMapping("/presenca/{id}")
    public PresencaDTO atualizarPresenca(@PathVariable Long id,
                                         @RequestBody HorasPresentesSubmitDto dto) {
        return chamadaService.atualizarPresenca(id, dto.horasPresentes());
    }

    /**
     * Lista todas as presenças de um dia específico.
     * GET /api/chamada/listar/{diaAulaId}
     */
    @GetMapping("/listar/{diaAulaId}")
    public List<PresencaDTO> listarPresencas(@PathVariable Long diaAulaId) {
        return chamadaService.listarPresencasPorDia(diaAulaId);
    }

    /**
     * Calcula a frequência de todos os alunos de uma turma.
     * GET /api/chamada/frequencia/{turmaId}
     */
    @GetMapping("/frequencia/{turmaId}")
    public List<FrequenciaDTO> calcularFrequencia(@PathVariable Long turmaId) {
        return chamadaService.calcularFrequenciaTurma(turmaId);
    }

    /**
     * Pesquisa alunos pelo nome e retorna percentual de presença e falta.
     * GET /api/chamada/frequencia/aluno?nome=XXX
     */
    @GetMapping("/frequencia/aluno")
    public List<FrequenciaAlunoDTO> buscarFrequenciaPorNome(@RequestParam String nome) {
        return chamadaService.buscarFrequenciaPorNome(nome);
    }
    
    @GetMapping("/relatorio/{turmaId}")
    public ResponseEntity<byte[]> gerarEbaixar(@PathVariable Long turmaId) {

        byte[] arquivo = arquivoService.gerarRelatorioFrequencia(turmaId);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=elatorioFrequencia.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(arquivo);
    }

}
