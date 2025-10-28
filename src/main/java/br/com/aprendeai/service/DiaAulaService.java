package br.com.aprendeai.service;

import java.util.List;
import br.com.aprendeai.dtos.DiaAulaCreateDTO;
import br.com.aprendeai.dtos.DiaAulaResponseDTO;

public interface DiaAulaService {
    DiaAulaResponseDTO criarDiaAula(DiaAulaCreateDTO dto);
    List<DiaAulaResponseDTO> listarPorTurma(Long turmaId);
    DiaAulaResponseDTO buscarPorId(Long id);
    void deletar(Long id);
}
