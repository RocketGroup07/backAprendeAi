package br.com.aprendeai.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.aprendeai.dtos.ComentarioCreateDto;
import br.com.aprendeai.dtos.ComentarioResponseDto;
import br.com.aprendeai.mappers.ComentarioMapper;
import br.com.aprendeai.model.Comentario;
import br.com.aprendeai.model.Post;
import br.com.aprendeai.model.Usuario;
import br.com.aprendeai.repository.ComentarioRepository;
import br.com.aprendeai.repository.PostRepository;
import br.com.aprendeai.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/comentarios")
@CrossOrigin
public class ComentarioController {

	private final ComentarioRepository comentarioRepo;
	private final PostRepository postRepo;
	private final UsuarioRepository usuarioRepo;
	private final ComentarioMapper comentarioMapper;
	
	public ComentarioController(ComentarioRepository comentarioRepo, PostRepository postRepo,
			UsuarioRepository usuarioRepo, ComentarioMapper comentarioMapper) {
		this.comentarioRepo = comentarioRepo;
		this.postRepo = postRepo;
		this.usuarioRepo = usuarioRepo;
		this.comentarioMapper = comentarioMapper;
	}
	
	@PostMapping("/criar/{usuarioId}/post/{postId}")
    public ComentarioResponseDto criarComentario(@PathVariable Long usuarioId,
                                                 @PathVariable Long postId,
                                                 @RequestBody ComentarioCreateDto comentarioDto) {

        Usuario usuario = usuarioRepo.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

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

    @GetMapping("/post/{postId}")
    public List<ComentarioResponseDto> listarComentarios(@PathVariable Long postId) {
        List<Comentario> comentarios = comentarioRepo.findByPostIdOrderByDataComentarioAsc(postId);
        return comentarios.stream()
                .map(comentarioMapper::toResponseDto)
                .toList();
    }

    @DeleteMapping("/{comentarioId}")
    public void deletarComentario(@PathVariable Long comentarioId) {
        comentarioRepo.deleteById(comentarioId);
    }
	
}
