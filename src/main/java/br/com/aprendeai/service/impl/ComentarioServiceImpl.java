package br.com.aprendeai.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.aprendeai.config.AccessControlService;
import br.com.aprendeai.dtos.ComentarioCreateDto;
import br.com.aprendeai.dtos.ComentarioResponseDto;
import br.com.aprendeai.mappers.ComentarioMapper;
import br.com.aprendeai.model.Comentario;
import br.com.aprendeai.model.Post;
import br.com.aprendeai.model.Turma;
import br.com.aprendeai.model.Usuario;
import br.com.aprendeai.repository.ComentarioRepository;
import br.com.aprendeai.repository.PostRepository;
import br.com.aprendeai.repository.TurmaRepository;
import br.com.aprendeai.service.ComentarioService;
import jakarta.transaction.Transactional;

@Service
public class ComentarioServiceImpl implements ComentarioService{
	
	private final ComentarioRepository comentarioRepo;
	private final PostRepository postRepo;
	private final ComentarioMapper comentarioMapper;
	private final AccessControlService accessControlService;
	private final TurmaRepository turmaRepository;

	public ComentarioServiceImpl(ComentarioRepository comentarioRepo, PostRepository postRepo,
			ComentarioMapper comentarioMapper, AccessControlService accessControlService,
			TurmaRepository turmaRepository) {
		super();
		this.comentarioRepo = comentarioRepo;
		this.postRepo = postRepo;
		this.comentarioMapper = comentarioMapper;
		this.accessControlService = accessControlService;
		this.turmaRepository = turmaRepository;
	}

	@Override
	@Transactional
	public ComentarioResponseDto criarComentario(Long turmaId, Long postId, ComentarioCreateDto comentarioDto) {
		
		Optional<Turma> turmaExiste = turmaRepository.findById(turmaId);
		
		if(turmaExiste.isEmpty()) {
			throw new IllegalArgumentException("Turma não encontrada.");
		}
		
		Turma turma = turmaExiste.get();
		
		accessControlService.verificarParticipacao(turma);
		
		Usuario usuario = accessControlService.getUsuarioLogado();

        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post não encontrado."));

        Comentario comentario = new Comentario();
        comentario.setUsuario(usuario);
        comentario.setPost(post);
        comentario.setConteudo(comentarioDto.conteudo());
        comentario.setDataComentario(LocalDateTime.now());

        Comentario salvo = comentarioRepo.save(comentario);

        return comentarioMapper.toResponseDto(salvo);
	}

	@Override
	@Transactional
	public List<ComentarioResponseDto> listarComentarios(Long postId) {
		List<Comentario> comentarios = comentarioRepo.findByPostIdOrderByDataComentarioAsc(postId);
        return comentarios.stream()
                .map(comentarioMapper::toResponseDto)
                .toList();
	}

	@Override
	@Transactional
	public void deletarComentario(Long comentarioId) {
		Usuario usuario = accessControlService.getUsuarioLogado();
    	
    	Comentario comentario = comentarioRepo.findById(comentarioId)
    			.orElseThrow(() -> new RuntimeException("Post não encontrado."));
    	
    	if(usuario != comentario.getUsuario() || usuario == comentario.getPost().getTurma().getProfessor()) {
    		throw new IllegalArgumentException("Apenas o autor do comentário ou o professor da turma pode apagá-lo.");
    	}
    	
        comentarioRepo.deleteById(comentarioId);
		
	}

}
