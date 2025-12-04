package br.com.aprendeai.service;

import br.com.aprendeai.config.AccessControlService;
import br.com.aprendeai.dtos.*;
import br.com.aprendeai.model.*;
import br.com.aprendeai.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChamadaService {

    private final DiaAulaRepository diaAulaRepository;
    private final PresencaRepository presencaRepository;
    private final TurmaRepository turmaRepository;
    private final AccessControlService accessControl;

    public ChamadaService(DiaAulaRepository diaAulaRepository, PresencaRepository presencaRepository,
			TurmaRepository turmaRepository, AccessControlService accessControl) {
		super();
		this.diaAulaRepository = diaAulaRepository;
		this.presencaRepository = presencaRepository;
		this.turmaRepository = turmaRepository;
		this.accessControl = accessControl;
	}

	// Inicializa a chamada do dia
    @Transactional
    public List<PresencaDTO> inicializarChamada(InicializarChamadaDTO dto) {
        Turma turma = turmaRepository.findById(dto.turmaId())
                .orElseThrow(() -> new EntityNotFoundException("Turma não encontrada"));
        
        accessControl.verificarAcessoProfessor(turma);

        if (dto.horasMaximas() <= 0 || dto.horasMaximas() > turma.getCargaHorariaTotal()) {
            throw new IllegalArgumentException("Horas máximas devem ser maiores que zero e não devem ultrapassar a carga horária total");
        }

        // Busca dia de aula existente ou cria novo corretamente
        DiaAula diaAula = diaAulaRepository.findByTurmaIdAndDataAula(dto.turmaId(), dto.dataAula())
                .map(existente -> {
                    // Atualiza as horas do dia caso tenha mudado
                    if (!existente.getHorasMaximas().equals(dto.horasMaximas())) {
                        existente.setHorasMaximas(dto.horasMaximas());
                        existente.setHorasTotais(dto.horasMaximas());
                        diaAulaRepository.save(existente);
                    }
                    return existente;
                })
                .orElseGet(() -> {
                    DiaAula novoDia = new DiaAula();
                    novoDia.setTurma(turma);
                    novoDia.setDataAula(dto.dataAula());
                    novoDia.setConteudo(dto.conteudo());
                    novoDia.setHorasMaximas(dto.horasMaximas());
                    novoDia.setHorasTotais(dto.horasMaximas());
                    return diaAulaRepository.save(novoDia);
                });


        List<Usuario> alunos = turma.getAlunos().stream().toList();

        // Cria presenças apenas se ainda não existirem
        List<Presenca> presencasExistentes = presencaRepository.findByDiaAulaId(diaAula.getId());
        if (presencasExistentes.isEmpty()) {
            presencasExistentes = alunos.stream()
                    .map(aluno -> new Presenca(diaAula, aluno, diaAula.getHorasMaximas()))
                    .collect(Collectors.toList());
            presencaRepository.saveAll(presencasExistentes);
        }

        return presencasExistentes.stream()
                .map(p -> new PresencaDTO(p.getId(), p.getAluno().getId(), p.getAluno().getNome(), p.getHorasPresente()))
                .toList();
    }

    // Atualiza a presença individual de um aluno
    @Transactional
    public PresencaDTO atualizarPresenca(Long presencaId, int horasPresente) {
        Presenca presenca = presencaRepository.findById(presencaId)
                .orElseThrow(() -> new EntityNotFoundException("Presença não encontrada"));

        int horasMax = presenca.getDiaAula().getHorasMaximas();
        if (horasPresente < 0 || horasPresente > horasMax) {
            throw new IllegalArgumentException("Horas presente deve estar entre 0 e " + horasMax);
        }

        presenca.setHorasPresente(horasPresente);
        presencaRepository.save(presenca);

        return new PresencaDTO(presenca.getId(), presenca.getAluno().getId(),
                presenca.getAluno().getNome(), presenca.getHorasPresente());
    }

    // Lista todas presenças de um dia de aula
    public List<PresencaDTO> listarPresencasPorDia(Long diaAulaId) {
        List<Presenca> presencas = presencaRepository.findByDiaAulaId(diaAulaId);

        if (presencas.isEmpty()) {
            throw new EntityNotFoundException("Nenhuma presença encontrada para este dia de aula");
        }

        return presencas.stream()
                .map(p -> new PresencaDTO(p.getId(), p.getAluno().getId(), p.getAluno().getNome(), p.getHorasPresente()))
                .toList();
    }

    // Calcula a frequência de todos os alunos de uma turma
//    public List<FrequenciaDTO> calcularFrequenciaTurma(Long turmaId) {
//        Turma turma = turmaRepository.findById(turmaId)
//                .orElseThrow(() -> new EntityNotFoundException("Turma não encontrada"));
//
//        int cargaHorariaTotal = turma.getCargaHorariaTotal();
//
//        return turma.getAlunos().stream()
//                .map(aluno -> {
//                    int horasPresente = presencaRepository.findByAlunoIdAndTurmaId(aluno.getId(), turmaId)
//                            .stream()
//                            .mapToInt(Presenca::getHorasPresente)
//                            .sum();
//
//                    double percentual = (cargaHorariaTotal > 0) ? (horasPresente * 100.0 / cargaHorariaTotal) : 0.0;
//
//                    return new FrequenciaDTO(aluno.getId(), aluno.getNome(), horasPresente, cargaHorariaTotal, percentual);
//                })
//                .toList();
//    }
    
    public List<FrequenciaDTO> calcularFrequenciaTurma(Long turmaId) {

        Turma turma = turmaRepository.findById(turmaId)
                .orElseThrow(() -> new EntityNotFoundException("Turma não encontrada"));

        int cargaHorariaTotal = turma.getCargaHorariaTotal();

        return turma.getAlunos().stream()
                .map(aluno -> {

                    int horasPresente = presencaRepository.findByAlunoIdAndTurmaId(aluno.getId(), turmaId)
                            .stream()
                            .mapToInt(Presenca::getHorasPresente)
                            .sum();
                    
                    double percentual = (cargaHorariaTotal > 0)
                            ? (horasPresente * 100.0 / cargaHorariaTotal)
                            : 0.0;
                    
                    return new FrequenciaDTO(
                            aluno.getId(),
                            aluno.getNome(),
                            horasPresente,
                            cargaHorariaTotal,
                            percentual
                    );
                })
                .sorted(Comparator.comparing(FrequenciaDTO::nomeAluno))
                .toList();
    }


    // Busca aluno por nome e calcula presença/falta com base na carga horária da turma
    public List<FrequenciaAlunoDTO> buscarFrequenciaPorNome(String nomeAluno) {
        List<Turma> turmas = turmaRepository.findAll();

        return turmas.stream()
                .flatMap(turma -> turma.getAlunos().stream()
                        .filter(aluno -> aluno.getNome().toLowerCase().contains(nomeAluno.toLowerCase()))
                        .map(aluno -> {
                            int cargaHorariaTotal = turma.getCargaHorariaTotal(); // vem da turma
                            int horasPresenteTotal = presencaRepository.findByAlunoIdAndTurmaId(aluno.getId(), turma.getId())
                                    .stream()
                                    .mapToInt(Presenca::getHorasPresente)
                                    .sum();
                            double percentualPresenca = (cargaHorariaTotal > 0)
                                    ? (horasPresenteTotal * 100.0 / cargaHorariaTotal)
                                    : 0.0;
                            double percentualFalta = 100.0 - percentualPresenca;

                            return new FrequenciaAlunoDTO(
                                    aluno.getId(),
                                    aluno.getNome(),
                                    horasPresenteTotal,
                                    cargaHorariaTotal,
                                    percentualPresenca,
                                    percentualFalta
                            );
                        })
                )
                .toList();
    }
}
