package br.com.aprendeai.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import br.com.aprendeai.dtos.DiaAulaCreateDTO;
import br.com.aprendeai.dtos.DiaAulaResponseDTO;
import br.com.aprendeai.mappers.DiaAulaMapper;
import br.com.aprendeai.mappers.TurmaMapper;
import br.com.aprendeai.model.DiaAula;
import br.com.aprendeai.model.Turma;
import br.com.aprendeai.repository.DiaAulaRepository;
import br.com.aprendeai.repository.TurmaRepository;
import br.com.aprendeai.service.DiaAulaService;

@Service
public class DiaAulaServiceImpl implements DiaAulaService {

    private final DiaAulaRepository diaAulaRepository;
    private final TurmaRepository turmaRepository;
    private final DiaAulaMapper diaAulaMapper;
    private final TurmaMapper turmaMapper;

    public DiaAulaServiceImpl(DiaAulaRepository diaAulaRepository, TurmaRepository turmaRepository,
			DiaAulaMapper diaAulaMapper, TurmaMapper turmaMapper) {
		this.diaAulaRepository = diaAulaRepository;
		this.turmaRepository = turmaRepository;
		this.diaAulaMapper = diaAulaMapper;
		this.turmaMapper = turmaMapper;
	}

	@Override
    @Transactional
    public DiaAulaResponseDTO criarDiaAula(DiaAulaCreateDTO dto) {
        Turma turma = turmaRepository.findById(dto.turmaId())
                .orElseThrow(() -> new RuntimeException("Turma não encontrada."));

//        DiaAula diaAula = diaAulaMapper.toEntity(dto);
//        diaAula.setTurma(turma);
//        diaAula.setHorasMaximas(dto.horasMaximas());
//        diaAula.setHorasTotais(dto.horasTotais());
//        diaAula.setDataAula(dto.dataAula());
        
        DiaAula diaAula = diaAulaRepository.findByTurmaIdAndDataAula(dto.turmaId(), dto.dataAula())
        	    .orElseGet(() -> {
        	        DiaAula novoDia = new DiaAula(dto.dataAula(), dto.conteudo(), dto.horasMaximas(), dto.horasMaximas(), turma);
        	        DiaAula salvo = diaAulaRepository.save(novoDia);
        	        System.out.println("DiaAula salvo com ID: " + salvo.getId());
        	        return salvo;
        	    });


        DiaAula salvo = diaAulaRepository.save(diaAula);
        return diaAulaMapper.toResponse(salvo);
    }

    @Override
    public List<DiaAulaResponseDTO> listarPorTurma(Long turmaId) {
        return diaAulaRepository.findByTurmaId(turmaId)
                .stream()
                .map(diaAulaMapper::toResponse)
                .toList();
    }

    @Override
    public DiaAulaResponseDTO buscarPorId(Long id) {
        DiaAula diaAula = diaAulaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dia de aula não encontrado."));
        return diaAulaMapper.toResponse(diaAula);
    }

    @Override
    public void deletar(Long id) {
        diaAulaRepository.deleteById(id);
    }
}
