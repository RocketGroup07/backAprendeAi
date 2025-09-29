package br.com.aprendeai.controller;

import br.com.aprendeai.model.Atividade;
import br.com.aprendeai.repository.AtividadeRepository;
import br.com.aprendeai.repository.UsuarioRepository;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/atividades")
@CrossOrigin
public class TelaAtividadesController {
	
	private AtividadeRepository atividadeRepository;
	
	private UsuarioRepository usuarioRepository;
	
	public TelaAtividadesController(AtividadeRepository atividadeRepository, UsuarioRepository usuarioRepository) {
		this.atividadeRepository = atividadeRepository;
		this.usuarioRepository = usuarioRepository;
	}

    @PostMapping("")
    public ResponseEntity<?> adicionarAtividade(@Valid @RequestBody Atividade atividade) {
    	
    	Atividade novaAtividade = atividadeRepository.save(atividade);
    	
    	return ResponseEntity.ok(novaAtividade);
    }

//    @GetMapping("")
//    public List<Atividade> listarAtividades() {
//        return atividadeRepository.findAll();
//    }

//    @GetMapping("")
//    public ResponseEntity<?> buscarAtividadePorTitulo(String titulo) {
//        for (Atividade a : atividades) {
//            if (a.getTitulo().equalsIgnoreCase(titulo)) {
//                return a;
//            }
//        }
//        return null;
//    }
//
//    @PostMapping("")
//    public boolean entregarAtividade(Atividade atividade, String comentario) {
//        if (atividade != null && !atividade.isEntregue()) {
//            atividade.setComentario(comentario);
//            atividade.entregar();
//            return true;
//        }
//        return false;
//    }
//
//    @DeleteMapping("")
//    public boolean removerAtividade(String titulo) {
//        Atividade atividade = buscarAtividadePorTitulo(titulo);
//        if (atividade != null) {
//            atividades.remove(atividade);
//            return true;
//        }
//        return false;
//    }
}