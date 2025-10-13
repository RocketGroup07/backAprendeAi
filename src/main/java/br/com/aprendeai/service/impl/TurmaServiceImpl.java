package br.com.aprendeai.service.impl;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import br.com.aprendeai.dtos.RequestCodigoTurmaDTO;
import br.com.aprendeai.dtos.TurmaCreateDto;
import br.com.aprendeai.dtos.TurmaResponseDto;
import br.com.aprendeai.dtos.TurmaUpdateDto;
import br.com.aprendeai.mappers.TurmaMapper;
import br.com.aprendeai.model.Turma;
import br.com.aprendeai.model.Usuario;
import br.com.aprendeai.repository.TurmaRepository;
import br.com.aprendeai.repository.UsuarioRepository;
import br.com.aprendeai.service.TurmaService;
import br.com.aprendeai.util.AuthenticatedUser;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class TurmaServiceImpl implements TurmaService{
	
	private final UsuarioRepository usuarioRepository;
    private final TurmaRepository turmaRepository;
    private final TurmaMapper turmaMapper;
    private final AuthenticatedUser authenticatedUser;
    
    public TurmaServiceImpl(UsuarioRepository usuarioRepository, TurmaRepository turmaRepository,
			TurmaMapper turmaMapper, AuthenticatedUser authenticatedUser) {
		this.usuarioRepository = usuarioRepository;
		this.turmaRepository = turmaRepository;
		this.turmaMapper = turmaMapper;
		this.authenticatedUser = authenticatedUser;
	}

	@Override
    @Transactional
    public TurmaResponseDto criarTurma(TurmaCreateDto dto) {
		Usuario professorAutenticado = authenticatedUser.getCurrentUser();
        Turma turma = turmaMapper.toEntityFromCreateDto(dto);
        turma.setProfessor(professorAutenticado);
        turma.setCodigo(gerarCodigoUnico());
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
    	Usuario usuario = getUsuarioLogado();
    	
    	Turma turma = buscarTurmaId(id);
    	
    	if (!usuarioParticipaDaTurma(usuario, turma)) {
            throw new AccessDeniedException("Você não tem permissão para acessar essa turma.");
        }
       
        return turmaMapper.toResponseDto(turma);
    }
    
    @Override
    @Transactional
    public List<TurmaResponseDto> listarTodas() {
    	Usuario usuario = getUsuarioLogado();

        return turmaRepository.findAll().stream()
                .filter(t -> usuarioParticipaDaTurma(usuario, t))
                .map(turmaMapper::toResponseDto)
                .toList();
    }
    
    @Override
    @Transactional
    public TurmaResponseDto atualizar(Long id, TurmaUpdateDto dto) {
        Turma turma = buscarTurmaId(id);
        verificarAcessoProfessor(turma); 
        turmaMapper.updateEntityFromUpdateDto(turma, dto);
        Turma atualizada = turmaRepository.save(turma);

        return turmaMapper.toResponseDto(atualizada);
    }
    
    @Override
    @Transactional
    public void deletar(Long id) {
    	Turma turma = buscarTurmaId(id);
    	verificarAcessoProfessor(turma);
        turmaRepository.deleteById(id);
    }
	
//    public boolean validarCodigo(RequestCodigoTurmaDTO codigoTurma) {
//        return turmaRepository.findByCodigo(codigoTurma.codigoTurma()).isPresent();
//    }

    @Override
    @Transactional
    public TurmaResponseDto clonarTurma(Long id) {
        Turma original = buscarTurmaId(id);
        
        verificarAcessoProfessor(original); 

        Turma clone = new Turma();
        clone.setNome(original.getNome() + " (Cópia)");
        clone.setLimiteAlunos(0);
        clone.setProfessor(original.getProfessor());
        clone.setCodigo(gerarCodigoUnico());
        clone.setCriadoEm(LocalDateTime.now());
        clone.setAlunos(new HashSet<>());

        return turmaMapper.toResponseDto(turmaRepository.save(clone));
    }

    @Override
    @Transactional
    public TurmaResponseDto adicionarAluno(RequestCodigoTurmaDTO codigo, Long alunoId) {
        Turma turma = turmaRepository.findByCodigo(codigo.codigoTurma())
                .orElseThrow(() -> new EntityNotFoundException("Turma não encontrada."));
        verificarAcessoProfessor(turma);

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
    
    private void verificarAcessoProfessor(Turma turma) {
        Usuario usuarioAutenticado = authenticatedUser.getCurrentUser();
        
        if (!turma.getProfessor().getId().equals(usuarioAutenticado.getId())) {
            throw new SecurityException("Apenas o professor da turma pode executar esta ação.");
        }
    }
    
    private Usuario getUsuarioLogado() {
        return authenticatedUser.getCurrentUser();
    }
    
    private boolean usuarioParticipaDaTurma(Usuario usuario, Turma turma) {
        return turma.getProfessor().getId().equals(usuario.getId()) || turma.getAlunos().contains(usuario);
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
