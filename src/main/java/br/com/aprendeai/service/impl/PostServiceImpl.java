package br.com.aprendeai.service.impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.aprendeai.config.AccessControlService;
import br.com.aprendeai.dtos.PostCreateDto;
import br.com.aprendeai.dtos.PostResponseDto;
import br.com.aprendeai.mappers.PostMapper;
import br.com.aprendeai.model.Arquivo;
import br.com.aprendeai.model.Atividade;
import br.com.aprendeai.model.Post;
import br.com.aprendeai.model.Turma;
import br.com.aprendeai.model.Usuario;
import br.com.aprendeai.repository.PostRepository;
import br.com.aprendeai.repository.TurmaRepository;
import br.com.aprendeai.repository.UsuarioRepository;
import br.com.aprendeai.service.ArquivoService;
import br.com.aprendeai.service.PostService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class PostServiceImpl implements PostService{
	
	private final PostRepository postRepo;
	private final UsuarioRepository userRep;
	private final TurmaRepository turmaRepo;
    private final ArquivoService arquivoService;
	private final PostMapper postMapper;
	private final AccessControlService accessControlService;
	private final ObjectMapper objectMapper;
	
	public PostServiceImpl(PostRepository postRepo, UsuarioRepository userRep, TurmaRepository turmaRepo,
			ArquivoService arquivoService, PostMapper postMapper, AccessControlService accessControlService) {
		this.postRepo = postRepo;
		this.userRep = userRep;
		this.turmaRepo = turmaRepo;
		this.arquivoService = arquivoService;
		this.postMapper = postMapper;
		this.accessControlService = accessControlService;
		
		this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}
	
	@Override
	@Transactional
	public PostResponseDto criarPost(Long usuarioId, Long turmaId, String post, MultipartFile arquivo) {
	  
	  Usuario autor = userRep.findById(usuarioId)
	                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
	        Turma turma = turmaRepo.findById(turmaId)
	                .orElseThrow(() -> new RuntimeException("Turma não encontrada."));
	        
	        Arquivo arquivoSalvo = null;
	        
	        accessControlService.usuarioParticipaDaTurma(autor, turma);
	        PostCreateDto postRequest = null;
	        try {
	            postRequest = objectMapper.readValue(post, PostCreateDto.class);
	        } catch (JsonProcessingException e) {
	            throw new RuntimeException(e);
	        }
	        Post novoPost = postMapper.toEntityFromCreateDto(postRequest);
	        novoPost.setAutor(autor);
	        novoPost.setTurma(turma);
	        novoPost.setTitulo(postRequest.titulo()); 
	        novoPost.setConteudo(postRequest.conteudo());
	        novoPost.setPublico(postRequest.publico() == null ? true : postRequest.publico());
	
	        if(postRequest.dataPostagem() == null) {
	         novoPost.setDataPostagem(LocalDateTime.now());
	        }else {
	         novoPost.setDataPostagem(postRequest.dataPostagem());
	        }
	       
	        if (arquivo != null && !arquivo.isEmpty()) {
	            arquivoSalvo = arquivoService.uploadArquivo(arquivo);
	            arquivoSalvo.setEnviadoPor(autor);
	            arquivoSalvo.setPost(novoPost);
	            novoPost.setArquivo(Arrays.asList(arquivoSalvo));
	        }
	        Post postSalvo = postRepo.save(novoPost);
	        return postMapper.toResponseDto(postSalvo);
	 }
	 
	
	@Override
	@Transactional
	public PostResponseDto buscarPostPorId(Long postId, Long turmaId) {
		
		Turma turma = turmaRepo.findById(turmaId)
                .orElseThrow(() -> new EntityNotFoundException("Turma não encontrada."));

        accessControlService.verificarParticipacao(turma);
		
	    Optional<Post> post = postRepo.findByIdAndTurmaId(postId, turmaId);
	    
	    if (post.isEmpty()) {
	        throw new EntityNotFoundException("Post não encontrado para a turma especificada.");
	    }

	    return postMapper.toResponseDto(post.get());
	}
	
	@Override
    @Transactional
    public Arquivo baixarAnexo(Long postId, Long turmaId) {
		 	Optional<Post> postExiste = postRepo.findByIdAndTurmaId(postId, turmaId);

		    if (postExiste.isEmpty()) {
		        throw new EntityNotFoundException("Post não encontrado com o id " + postId);
		    }
		    
		    Post post = postExiste.get();
	        
	        Turma turma = post.getTurma();
	        
	        accessControlService.verificarParticipacao(turma);

	        if (post.getArquivo() == null || post.getArquivo().isEmpty()) {
	            throw new EntityNotFoundException("Nenhum anexo encontrado para esta atividade.");
	        }

	        Arquivo arquivo = post.getArquivo().get(0);
	        return arquivo;
	    }
	
	@Override
	@Transactional
	public List<PostResponseDto> listarPostsDaTurma(Long turmaId) {
		
		Turma turma = turmaRepo.findById(turmaId)
                .orElseThrow(() -> new EntityNotFoundException("Turma não encontrada."));
        
		accessControlService.verificarParticipacao(turma);
		
		List<Post> posts = postRepo.findPublicadosByTurmaId(turmaId, LocalDateTime.now());
	    return posts.stream()
	                .map(postMapper::toResponseDto)
	                .toList();
	}
	
	@Override
	@Transactional
	public List<PostResponseDto> buscarNaTurmaPeloTitulo(Long turmaId, String titulo) {
		
		Turma turma = turmaRepo.findById(turmaId)
                .orElseThrow(() -> new EntityNotFoundException("Turma não encontrada."));
        
		accessControlService.verificarParticipacao(turma);
		
		List<Post> posts = postRepo.buscarPublicadosByTitulo(turmaId, titulo, LocalDateTime.now());
				return posts.stream()
						.map(postMapper::toResponseDto)
						.toList();
	}
	
	@Override
	@Transactional
	public void deletarPost(Long turmaId, Long postId) {
	    Optional<Post> postExiste = postRepo.findByIdAndTurmaId(postId, turmaId);

	    if (postExiste.isEmpty()) {
	        throw new EntityNotFoundException("Post não encontrado com o id " + postId);
	    }
	    
	    Post postParaDeletar = postExiste.get();
        
        Usuario usuarioLogado = accessControlService.getUsuarioLogado();
        
        boolean isProfessor = postParaDeletar.getTurma().getProfessor().getId().equals(usuarioLogado.getId());

        boolean isAutor = postParaDeletar.getAutor().getId().equals(usuarioLogado.getId());
        
        if (!isAutor || !isProfessor) {
            throw new SecurityException("Apenas o professor da turma ou o autor do post podem deletá-lo.");
        }

	    postRepo.delete(postExiste.get());
	}
}
