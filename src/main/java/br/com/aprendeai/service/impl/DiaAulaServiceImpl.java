package br.com.aprendeai.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import br.com.aprendeai.dtos.DiaAulaCreateDTO;
import br.com.aprendeai.dtos.DiaAulaResponseDTO;
import br.com.aprendeai.mappers.DiaAulaMapper;
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

    public DiaAulaServiceImpl(DiaAulaRepository diaAulaRepository, TurmaRepository turmaRepository,
			DiaAulaMapper diaAulaMapper) {
		this.diaAulaRepository = diaAulaRepository;
		this.turmaRepository = turmaRepository;
		this.diaAulaMapper = diaAulaMapper;
	}

	@Override
    @Transactional
    public DiaAulaResponseDTO criarDiaAula(DiaAulaCreateDTO dto) {
        Turma turma = turmaRepository.findById(dto.turmaId())
                .orElseThrow(() -> new RuntimeException("Turma não encontrada."));

        if (dto.horasMaximas() <= 0 || dto.horasMaximas() > 8) {
            throw new IllegalArgumentException("Horas máximas devem ser maiores que zero e menores que 8.");
        }

        int cargaHorariaTotal = turma.getCargaHorariaTotal();

        DiaAula diaAulaExistente = diaAulaRepository.findByTurmaIdAndDataAula(dto.turmaId(), dto.dataAula()).orElse(null);
        
        int horasTotaisJaRegistradas = diaAulaRepository.findByTurmaId(turma.getId()).stream()
                .mapToInt(DiaAula::getHorasMaximas)
                .sum();
        
        int horasNovas = dto.horasMaximas();
        int horasAntesDaAtualizacao = 0;
        
        if (diaAulaExistente != null) {
            horasAntesDaAtualizacao = diaAulaExistente.getHorasMaximas();
            horasTotaisJaRegistradas -= horasAntesDaAtualizacao;
        }

        int horasAposAdicao = horasTotaisJaRegistradas + horasNovas;

        if (horasAposAdicao > cargaHorariaTotal) {
            int horasRestantes = cargaHorariaTotal - horasTotaisJaRegistradas;
            String mensagem = String.format(
                "A adição de %d horas ultrapassa a carga horária total da turma (%d horas). Você só pode adicionar no máximo mais %d horas.",
                horasNovas, cargaHorariaTotal, horasRestantes
            );
            throw new IllegalArgumentException(mensagem);
        }
        
        DiaAula diaAula = diaAulaRepository.findByTurmaIdAndDataAula(dto.turmaId(), dto.dataAula())
        	    .orElseGet(() -> {
        	        DiaAula novoDia = new DiaAula(dto.dataAula(), dto.conteudo(), dto.horasMaximas(), dto.horasTotais(), turma);
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
