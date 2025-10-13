package br.com.aprendeai.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.aprendeai.dtos.AuthDto;
import br.com.aprendeai.dtos.LoginResponseDto;
import br.com.aprendeai.dtos.TurmaResponseDto;
import br.com.aprendeai.dtos.UsuarioCreateDto;
import br.com.aprendeai.dtos.UsuarioResponseDto;
import br.com.aprendeai.dtos.UsuarioUpdateDto;
import br.com.aprendeai.enums.PapelEnum;
import br.com.aprendeai.mappers.TurmaMapper;
import br.com.aprendeai.mappers.UsuarioMapper;
import br.com.aprendeai.model.Turma;
import br.com.aprendeai.model.Usuario;
import br.com.aprendeai.repository.TurmaRepository;
import br.com.aprendeai.repository.UsuarioRepository;
import br.com.aprendeai.service.AlunoService;
import br.com.aprendeai.service.AutenticacaoService;
import br.com.aprendeai.util.AuthenticatedUser;

@Service
public class AlunoServiceImpl implements AlunoService {

    private final UsuarioRepository usuarioRepository;
    private final TurmaRepository turmaRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;
    private final AutenticacaoService autenticacaoService;
    private final AuthenticatedUser authenticatedUser;
    private final TurmaMapper turmaMapper;

	public AlunoServiceImpl(UsuarioRepository usuarioRepository, TurmaRepository turmaRepository,
			UsuarioMapper usuarioMapper, PasswordEncoder passwordEncoder, AutenticacaoService autenticacaoService,
			AuthenticatedUser authenticatedUser, TurmaMapper turmaMapper) {
		this.usuarioRepository = usuarioRepository;
		this.turmaRepository = turmaRepository;
		this.usuarioMapper = usuarioMapper;
		this.passwordEncoder = passwordEncoder;
		this.autenticacaoService = autenticacaoService;
		this.authenticatedUser = authenticatedUser;
		this.turmaMapper = turmaMapper;
	}

	@Override
    @Transactional
    public LoginResponseDto cadastrarAlunoComTurma(UsuarioCreateDto dto, String codigoTurma) {
        Turma turma = turmaRepository.findByCodigo(codigoTurma)
                .orElseThrow(() -> new RuntimeException("Código de turma inválido."));

        if (usuarioRepository.existsByLogin(dto.login())) {
            throw new RuntimeException("O email inserido já está em uso.");
        }

        Usuario novoAluno = usuarioMapper.toEntityFromCreateDto(dto);
        novoAluno.setSenha(passwordEncoder.encode(dto.senha()));
        novoAluno.setPapel(PapelEnum.USER);
        novoAluno.setCriadoEm(LocalDateTime.now());

        Usuario alunoSalvo = usuarioRepository.save(novoAluno);
        
        if (!turma.getAlunos().contains(alunoSalvo)) {
            turma.getAlunos().add(alunoSalvo);
            turma.getAlunos().size();
            turmaRepository.save(turma);
        }

        AuthDto authDto = new AuthDto(dto.login(), dto.senha());
        String token = autenticacaoService.obterToken(authDto);

        return new LoginResponseDto(token, usuarioMapper.toResponseDTO(alunoSalvo),
                "Aluno cadastrado, adicionado à turma e autenticado com sucesso!");
    }
    
    @Override
    public List<TurmaResponseDto> encontrarTurmasDoAluno() {
    	Usuario aluno = authenticatedUser.getCurrentUser();
    	
    	return turmaRepository.findByAlunos_Id(aluno.getId())
                .stream()
                .map(turmaMapper::toResponseDto)
                .toList();
    }
    
    public String entrarEmTurma(String codigoTurma) {
    	
    	Turma turma = turmaRepository.findByCodigo(codigoTurma)
    			.orElseThrow(() -> new RuntimeException("Código de turma inválida."));
    	
    	Usuario aluno = authenticatedUser.getCurrentUser();
        
        if (turma.getAlunos().contains(aluno)) {
        	return "Aluno já faz parte desta turma.";
        } else if(turma.getAlunos().size() == turma.getLimiteAlunos()){
        	return "Turma cheia.";
        } else {
        	turma.getAlunos().add(aluno);
            
            turmaRepository.save(turma);
        }
        
        return "Aluno cadastrado com sucesso!";
    }

    @Override
    @Transactional
    public UsuarioResponseDto criarAluno(UsuarioCreateDto dto) {
        if (usuarioRepository.existsByLogin(dto.login())) {
            throw new RuntimeException("O email inserido já está em uso.");
        }

        Usuario aluno = usuarioMapper.toEntityFromCreateDto(dto);
        aluno.setSenha(passwordEncoder.encode(dto.senha()));
        aluno.setPapel(PapelEnum.USER);
        aluno.setCriadoEm(LocalDateTime.now());

        Usuario salvo = usuarioRepository.save(aluno);
        return usuarioMapper.toResponseDTO(salvo);
    }

    @Override
    public List<UsuarioResponseDto> listarAlunos() {
        return usuarioRepository.findByPapel(PapelEnum.USER)
                .stream()
                .map(usuarioMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UsuarioResponseDto buscarAlunoPorId(Long id) {
        Usuario aluno = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado."));

        if (aluno.getPapel() != PapelEnum.USER) {
            throw new RuntimeException("O usuário não tem o papel de ALUNO.");
        }

        return usuarioMapper.toResponseDTO(aluno);
    }

    @Override
    @Transactional
    public UsuarioResponseDto atualizarAluno(Long id, UsuarioUpdateDto dto) {
    	Usuario alunoAutenticado = authenticatedUser.getCurrentUser();
    	
    	if (!alunoAutenticado.getId().equals(id)) {
            throw new SecurityException("Operação negada.");
        }
    	
        Usuario aluno = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado."));

        if (aluno.getPapel() != PapelEnum.USER) {
            throw new RuntimeException("O usuário não tem o papel de ALUNO.");
        }

        if (!aluno.getLogin().equals(dto.login()) && usuarioRepository.existsByLogin(dto.login())) {
            throw new RuntimeException("O email inserido já está em uso.");
        }

        usuarioMapper.updateEntityFromUpdateDto(aluno, dto);

        if (dto.senha() != null && !dto.senha().isBlank()) {
            aluno.setSenha(passwordEncoder.encode(dto.senha()));
        }

        Usuario atualizado = usuarioRepository.save(aluno);
        return usuarioMapper.toResponseDTO(atualizado);
    }

    @Override
    @Transactional
    public void deletarAluno(Long id) {
    	Usuario alunoAutenticado = authenticatedUser.getCurrentUser();
    	
    	if (!alunoAutenticado.getId().equals(id)) {
            throw new SecurityException("Operação negada.");
        }
    	
        Usuario aluno = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado."));

        if (aluno.getPapel() != PapelEnum.USER) {
            throw new RuntimeException("O usuário não tem o papel de ALUNO.");
        }

        usuarioRepository.delete(aluno);
    }
}
