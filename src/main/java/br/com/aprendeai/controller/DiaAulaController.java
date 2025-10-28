package br.com.aprendeai.controller;

import br.com.aprendeai.dtos.DiaAulaCreateDTO;
import br.com.aprendeai.dtos.DiaAulaResponseDTO;
import br.com.aprendeai.service.DiaAulaService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/dia-aula")
@CrossOrigin(origins = "*")
public class DiaAulaController {

    private final DiaAulaService diaAulaService;

    public DiaAulaController(DiaAulaService diaAulaService) {
        this.diaAulaService = diaAulaService;
    }

    @PostMapping
    public DiaAulaResponseDTO criarDiaAula(@RequestBody DiaAulaCreateDTO dto) {
        return diaAulaService.criarDiaAula(dto);
    }

    @GetMapping("/turma/{turmaId}")
    public List<DiaAulaResponseDTO> listarPorTurma(@PathVariable Long turmaId) {
        return diaAulaService.listarPorTurma(turmaId);
    }

    @GetMapping("/{id}")
    public DiaAulaResponseDTO buscarPorId(@PathVariable Long id) {
        return diaAulaService.buscarPorId(id);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        diaAulaService.deletar(id);
    }
}
