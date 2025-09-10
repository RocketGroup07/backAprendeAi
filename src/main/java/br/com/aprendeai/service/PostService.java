//package br.com.aprendeai.service;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import br.com.aprendeai.dtos.PostRequestDTO;
//import br.com.aprendeai.model.Post;
//import br.com.aprendeai.model.PostArquivo;
//import br.com.aprendeai.model.Turma;
//import br.com.aprendeai.model.Usuario;
//import br.com.aprendeai.repository.PostRepository;
//import br.com.aprendeai.repository.TurmaRepository;
//import br.com.aprendeai.repository.UsuarioRepository;
//import lombok.RequiredArgsConstructor;
//
//@Service
//@RequiredArgsConstructor
//public class PostService {
//
//    private final PostRepository postRepository;
//    private final TurmaRepository turmaRepository;
//    private final UsuarioRepository usuarioRepository;
//
//    public Post criarPost(PostRequestDTO request, List<MultipartFile> arquivos) {
//
//        Turma turma = turmaRepository.findById(request.getTurmaId())
//                .orElseThrow(() -> new RuntimeException("Turma não encontrada"));
//
//        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
//                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
//
//        Post post = new Post();
//        post.setTitulo(request.getTitulo());
//        post.setConteudo(request.getConteudo());
//        post.setPublico(request.isPublico());
//        post.setTurmas(turma);
//        post.setUsuario(usuario);
//        post.setDataPostagem(LocalDateTime.now());
//
//        if (arquivos != null) {
//            List<PostArquivo> postArquivos = new ArrayList<>();
//            for (MultipartFile file : arquivos) {
//                String caminho = salvarArquivoLocal(file); 
//                PostArquivo arquivo = new PostArquivo();
//                arquivo.setNomeArquivo(file.getOriginalFilename());
//                arquivo.setCaminho(caminho);
//                arquivo.setPost(post);
//                postArquivos.add(arquivo);
//            }
//            post.setArquivos(postArquivos);
//        }
//
//        return postRepository.save(post);
//    }
//
//    private String salvarArquivoLocal(MultipartFile file) {
//        try {
//            String pasta = "uploads/";
//            Files.createDirectories(Paths.get(pasta));
//            String caminho = pasta + UUID.randomUUID() + "_" + file.getOriginalFilename();
//            file.transferTo(new File(caminho));
//            return caminho;
//        } catch (IOException e) {
//            throw new RuntimeException("Erro ao salvar arquivo", e);
//        }
//    }
//}
