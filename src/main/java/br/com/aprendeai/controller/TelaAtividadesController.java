package br.com.aprendeai.controller;

import br.com.aprendeai.model.Atividade;
import br.com.aprendeai.repository.AtividadeRepository;
import br.com.aprendeai.repository.UsuarioRepository;
import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/atividades")
@CrossOrigin
public class TelaAtividadesController {
	
	private AtividadeRepository atividadeRepository;
	
	public TelaAtividadesController(AtividadeRepository atividadeRepository, UsuarioRepository usuarioRepository) {
		this.atividadeRepository = atividadeRepository;
		this.usuarioRepository = usuarioRepository;
	}

	private UsuarioRepository usuarioRepository;

    @PostMapping("")
    public ResponseEntity<?> adicionarAtividade(@Valid @RequestBody Atividade atividade) {
        // Evita duplicidade pelo título
    	
    	Atividade novaAtividade = atividadeRepository.save(atividade);
    	
//        for (Atividade a : atividades) {
//            if (a.getTitulo().equalsIgnoreCase(atividade.getTitulo())) {
//                return false;
//            }
//        }
//        atividades.add(atividade);
//        return true;
    	
    	return ResponseEntity.ok(novaAtividade);
    }

//    @GetMapping("")
//    public List<Atividade> listarAtividades() {
//        return Collections.unmodifiableList(atividades); // Retorna lista imutável
//    }
//
//    @GetMapping("")
//    public Atividade buscarAtividadePorTitulo(String titulo) {
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