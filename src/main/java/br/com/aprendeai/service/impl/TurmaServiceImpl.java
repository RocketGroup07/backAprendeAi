package br.com.aprendeai.service.impl;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import br.com.aprendeai.config.AccessControlService;
import br.com.aprendeai.dtos.RequestCodigoTurmaDTO;
import br.com.aprendeai.dtos.TurmaCreateDto;
import br.com.aprendeai.dtos.TurmaResponseDto;
import br.com.aprendeai.dtos.TurmaUpdateDto;
import br.com.aprendeai.enums.PapelEnum;
import br.com.aprendeai.mappers.TurmaMapper;
import br.com.aprendeai.model.Arquivo;
import br.com.aprendeai.model.Atividade;
import br.com.aprendeai.model.Turma;
import br.com.aprendeai.model.Usuario;
import br.com.aprendeai.repository.AtividadeRepository;
import br.com.aprendeai.repository.TurmaRepository;
import br.com.aprendeai.repository.UsuarioRepository;
import br.com.aprendeai.service.TurmaService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class TurmaServiceImpl implements TurmaService{
	
	private final UsuarioRepository usuarioRepository;
    private final TurmaRepository turmaRepository;
    private final TurmaMapper turmaMapper;
    private final AccessControlService accessControl;
    private final AtividadeRepository atividadeRepository;

	public TurmaServiceImpl(UsuarioRepository usuarioRepository, TurmaRepository turmaRepository,
			TurmaMapper turmaMapper, AccessControlService accessControl, AtividadeRepository atividadeRepository) {
		super();
		this.usuarioRepository = usuarioRepository;
		this.turmaRepository = turmaRepository;
		this.turmaMapper = turmaMapper;
		this.accessControl = accessControl;
		this.atividadeRepository = atividadeRepository;
	}

	@Override
    @Transactional
    public TurmaResponseDto criarTurma(TurmaCreateDto dto) {
		Usuario professorAutenticado = accessControl.getUsuarioLogado();
		
		if(!professorAutenticado.getPapel().equals(PapelEnum.ADMIN)) {
			throw new AccessDeniedException("Apenas usuários ADMIN podem criar turmas.");
		}
		
        Turma turma = turmaMapper.toEntityFromCreateDto(dto);
        turma.setProfessor(professorAutenticado);
        turma.setCodigo(gerarCodigoUnico());
        turma.setCriadoEm(LocalDateTime.now());
        Turma salva = turmaRepository.save(turma);
        return turmaMapper.toResponseDto(salva);
    }
    
    @Override
    @Transactional
    public TurmaResponseDto validarCodigo(RequestCodigoTurmaDTO codigo) {
    	Turma turmaBuscada = turmaRepository.findByCodigo(codigo.codigoTurma())
    			.orElseThrow(() -> new EntityNotFoundException("Turma não encontrada com o código: " + codigo));
    	return turmaMapper.toResponseDto(turmaBuscada);
    }
    
    @Override           
    @Transactional
    public TurmaResponseDto buscarPorId(Long id) {
    	Usuario usuario = accessControl.getUsuarioLogado();
    	
    	Turma turma = buscarTurmaId(id);
    	
    	if (!accessControl.usuarioParticipaDaTurma(usuario, turma)) {
            throw new AccessDeniedException("Você não tem permissão para acessar essa turma.");
        }
       
        return turmaMapper.toResponseDto(turma);
    }
    
    @Override
    @Transactional
    public List<TurmaResponseDto> listarTodas() {
    	Usuario usuario = accessControl.getUsuarioLogado();

    	System.out.println("======================================================");
    	System.out.println("Encontrando turma do sistema....");
    	
        return turmaRepository.findAll().stream()
                .filter(t -> accessControl.usuarioParticipaDaTurma(usuario, t))
                .map(turmaMapper::toResponseDto)
                .toList();
    }
    
    @Override
    @Transactional
    public TurmaResponseDto atualizar(Long id, TurmaUpdateDto dto) {
        Turma turma = buscarTurmaId(id);
        accessControl.verificarAcessoProfessor(turma);
        turmaMapper.updateEntityFromUpdateDto(turma, dto);
        Turma atualizada = turmaRepository.save(turma);

        return turmaMapper.toResponseDto(atualizada);
    }
    
    @Override
    @Transactional
    public void deletar(Long id) {
    	Turma turma = buscarTurmaId(id);
    	accessControl.verificarAcessoProfessor(turma);
        turmaRepository.deleteById(id);
    }

//    @Override
//    @Transactional
//    public TurmaResponseDto clonarTurma(Long id) {
//        Turma original = buscarTurmaId(id);
//        
//        accessControl.verificarAcessoProfessor(original); 
//
//        Turma clone = new Turma();
//        clone.setNome(original.getNome() + " (Cópia)");
//        clone.setLimiteAlunos(0);
//        clone.setProfessor(original.getProfessor());
//        clone.setCodigo(gerarCodigoUnico());
//        clone.setCriadoEm(LocalDateTime.now());
//        clone.setAlunos(new HashSet<>());
//
//        return turmaMapper.toResponseDto(turmaRepository.save(clone));
//    }
    
    @Override
    @Transactional
    public TurmaResponseDto clonarTurma(Long id) {
        Turma original = buscarTurmaId(id);
        
        accessControl.verificarAcessoProfessor(original); 

        Turma clone = new Turma();
        clone.setNome(original.getNome() + " (Cópia)");
        clone.setLimiteAlunos(0);
        clone.setCodigo(gerarCodigoUnico());
        clone.setCargaHorariaTotal(original.getCargaHorariaTotal());
        clone.setProfessor(original.getProfessor());
        clone.setCriadoEm(LocalDateTime.now());
        clone.setAlunos(new HashSet<>());

        List<Atividade> atividadesClonadas = original.getAtividades().stream()
        	    .map(atividadeOriginal -> {
        	        Atividade atividadeClone = new Atividade();
        	        atividadeClone.setTitulo(atividadeOriginal.getTitulo());
        	        atividadeClone.setDataAtividade(atividadeOriginal.getDataAtividade());
        	        atividadeClone.setDataEntrega(atividadeOriginal.getDataEntrega());
        	        atividadeClone.setConteudo(atividadeOriginal.getConteudo());
        	        atividadeClone.setProfessor(atividadeOriginal.getProfessor());
        	        atividadeClone.setTurma(clone);
        	        atividadeClone.setEntregas(new ArrayList<>());

        	        List<Arquivo> anexosClonados = atividadeOriginal.getArquivoAnexo().stream()
        	            .map(arquivoOriginal -> {
        	                Arquivo arquivoClone = new Arquivo();
        	                arquivoClone.setNomeArquivo(arquivoOriginal.getNomeArquivo());
        	                arquivoClone.setCaminhoArquivo(arquivoOriginal.getCaminhoArquivo());
        	                return arquivoClone;
        	            }).collect(Collectors.toList());
        	        atividadeClone.setArquivoAnexo(anexosClonados);

        	        return atividadeClone;
        	    }).collect(Collectors.toList());


        clone.setAtividades(atividadesClonadas);

        Turma turmaSalva = turmaRepository.save(clone);
        atividadeRepository.saveAll(atividadesClonadas);

        return turmaMapper.toResponseDto(turmaSalva);
    }



    @Override
    @Transactional
    public TurmaResponseDto adicionarAluno(RequestCodigoTurmaDTO codigo, Long alunoId) {
        Turma turma = turmaRepository.findByCodigo(codigo.codigoTurma())
                .orElseThrow(() -> new EntityNotFoundException("Turma não encontrada."));
        accessControl.verificarAcessoProfessor(turma);

        Usuario aluno = buscarUsuarioId(alunoId);
        
        if (turma.getAlunos().size() >= turma.getLimiteAlunos()) {
            throw new IllegalStateException("A quantidade de alunos nessa turma já está completa.");
        }

        turma.getAlunos().add(aluno);
        return turmaMapper.toResponseDto(turmaRepository.save(turma));
    }

    @Override
    @Transactional
    public TurmaResponseDto removerAluno(Long turmaId, Long alunoId) {
        Turma turma = buscarTurmaId(turmaId);
        
        accessControl.verificarAcessoProfessor(turma);
        
        Usuario aluno = buscarUsuarioId(alunoId);

        if (!turma.getAlunos().contains(aluno)) {
            throw new IllegalArgumentException("Aluno não está nesta turma.");
        }

        turma.getAlunos().remove(aluno);
        return turmaMapper.toResponseDto(turmaRepository.save(turma));
    }

    private String gerarCodigoUnico() {
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        String codigo;

        do {
            StringBuilder sb = new StringBuilder(8);
            for (int i = 0; i < 8; i++) {
                sb.append(chars.charAt(random.nextInt(chars.length())));
            }
            codigo = sb.toString();
        } while (turmaRepository.existsByCodigo(codigo));

        return codigo;
    }
    
    private Turma buscarTurmaId(Long id) {
        return turmaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Turma não encontrada com id: " + id));
    }

    private Usuario buscarUsuarioId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com id: " + id));
    }
}
