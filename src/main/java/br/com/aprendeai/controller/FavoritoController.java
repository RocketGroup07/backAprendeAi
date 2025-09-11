package br.com.aprendeai.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.aprendeai.model.Favorito;
import br.com.aprendeai.model.Post;
import br.com.aprendeai.model.Usuario;
import br.com.aprendeai.repository.FavoritoRepository;
import br.com.aprendeai.repository.PostRepository;
import br.com.aprendeai.repository.UsuarioRepository;

@RestController
@RequestMapping("/favoritos")
@CrossOrigin
public class FavoritoController {

	@Autowired
    private  FavoritoRepository favoritoRepo;
    
	@Autowired
	private  UsuarioRepository usuarioRepo;
    
	@Autowired
	private  PostRepository postRepo;

    @PostMapping("/adicionar/{usuarioId}/{postId}")
    public Favorito favoritar(@PathVariable Long usuarioId, @PathVariable Long postId) {
        Usuario usuario = usuarioRepo.findById(usuarioId).orElseThrow();
        Post post = postRepo.findById(postId).orElseThrow();

        Favorito favorito = new Favorito();
        favorito.setUsuario(usuario);
        favorito.setPost(post);

        return favoritoRepo.save(favorito);
    }

    @GetMapping("/listar/{usuarioId}")
    public List<Favorito> listarFavoritos(@PathVariable Long usuarioId) {
        Usuario usuario = usuarioRepo.findById(usuarioId).orElseThrow();
        return favoritoRepo.findByUsuario(usuario);
    }
}