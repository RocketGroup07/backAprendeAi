package br.com.aprendeai.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.aprendeai.dtos.UsuarioCreateDto;
import br.com.aprendeai.dtos.UsuarioResponseDto;
import br.com.aprendeai.dtos.UsuarioUpdateDto;
import br.com.aprendeai.enums.PapelEnum;
import br.com.aprendeai.mappers.UsuarioMapper;
import br.com.aprendeai.model.Usuario;
import br.com.aprendeai.repository.UsuarioRepository;
import br.com.aprendeai.service.AutenticacaoService;
import br.com.aprendeai.service.ProfessorService;

@Service
public class ProfessorServiceImpl implements ProfessorService{
	
	private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    public ProfessorServiceImpl(UsuarioRepository usuarioRepository,
                            UsuarioMapper usuarioMapper,
                            PasswordEncoder passwordEncoder,
                            AutenticacaoService autenticacaoService) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    @Transactional
    public UsuarioResponseDto criarProfessor(UsuarioCreateDto dto) {
        if (usuarioRepository.existsByLogin(dto.login())) {
            throw new RuntimeException("O email inserido já está em uso.");
        }

        Usuario professor = usuarioMapper.toEntityFromCreateDto(dto);
        professor.setSenha(passwordEncoder.encode(dto.senha()));
        professor.setPapel(PapelEnum.ADMIN);
        professor.setCriadoEm(LocalDateTime.now());

        Usuario salvo = usuarioRepository.save(professor);
        return usuarioMapper.toResponseDTO(salvo);
    }
    
    @Override
    public List<UsuarioResponseDto> listarProfessores() {
        return usuarioRepository.findByPapel(PapelEnum.ADMIN)
                .stream()
                .map(usuarioMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UsuarioResponseDto buscarProfessorPorId(Long id) {
        Usuario professor = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado."));

        if (professor.getPapel() != PapelEnum.ADMIN) {
            throw new RuntimeException("O usuário não tem o papel de PROFESSOR.");
        }

        return usuarioMapper.toResponseDTO(professor);
    }

    @Override
    @Transactional
    public UsuarioResponseDto atualizarProfessor(Long id, UsuarioUpdateDto dto) {
        Usuario professor = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado."));

        if (professor.getPapel() != PapelEnum.ADMIN) {
            throw new RuntimeException("O usuário não tem o papel de PROFESSOR.");
        }

        if (!professor.getLogin().equals(dto.login()) && usuarioRepository.existsByLogin(dto.login())) {
            throw new RuntimeException("O email inserido já está em uso.");
        }

        usuarioMapper.updateEntityFromUpdateDto(professor, dto);

        if (dto.senha() != null && !dto.senha().isBlank()) {
            professor.setSenha(passwordEncoder.encode(dto.senha()));
        }

        Usuario atualizado = usuarioRepository.save(professor);
        return usuarioMapper.toResponseDTO(atualizado);
    }

    @Override
    @Transactional
    public void deletarProfessor(Long id) {
        Usuario professor = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado."));

        if (professor.getPapel() != PapelEnum.ADMIN) {
            throw new RuntimeException("O usuário não tem o papel de PROFESSOR.");
        }

        usuarioRepository.delete(professor);
    }

}
