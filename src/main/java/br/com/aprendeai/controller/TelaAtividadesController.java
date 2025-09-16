package br.com.aprendeai.controller;

import br.com.aprendeai.model.Atividade;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TelaAtividadesController {
    private List<Atividade> atividades = new ArrayList<>();

    public boolean adicionarAtividade(Atividade atividade) {
        // Evita duplicidade pelo título
        for (Atividade a : atividades) {
            if (a.getTitulo().equalsIgnoreCase(atividade.getTitulo())) {
                return false;
            }
        }
        atividades.add(atividade);
        return true;
    }

    public List<Atividade> listarAtividades() {
        return Collections.unmodifiableList(atividades); // Retorna lista imutável
    }

    public Atividade buscarAtividadePorTitulo(String titulo) {
        for (Atividade a : atividades) {
            if (a.getTitulo().equalsIgnoreCase(titulo)) {
                return a;
            }
        }
        return null;
    }

    public boolean entregarAtividade(Atividade atividade, String comentario) {
        if (atividade != null && !atividade.isEntregue()) {
            atividade.setComentario(comentario);
            atividade.entregar();
            return true;
        }
        return false;
    }

    public boolean removerAtividade(String titulo) {
        Atividade atividade = buscarAtividadePorTitulo(titulo);
        if (atividade != null) {
            atividades.remove(atividade);
            return true;
        }
        return false;
    }
}