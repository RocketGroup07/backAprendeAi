package br.com.aprendeai.controller;

import br.com.aprendeai.model.Atividade;
import java.util.ArrayList;
import java.util.List;

public class TelaAtividadesController {
    private List<Atividade> atividades = new ArrayList<>();

    public void adicionarAtividade(Atividade atividade) {
        atividades.add(atividade);
    }

    public List<Atividade> listarAtividades() {
        return atividades;
    }

    public void entregarAtividade(Atividade atividade, String comentario) {
        atividade.setComentario(comentario);
        atividade.entregar();
    }
}