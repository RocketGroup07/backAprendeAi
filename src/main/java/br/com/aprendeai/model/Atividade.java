package br.com.aprendeai.model;

public class Atividade {
    private String titulo;
    private String dataPost;
    private String dataEntrega;
    private String comentario;
    private boolean entregue;

    public Atividade(String titulo, String dataPost, String dataEntrega) {
        this.titulo = titulo;
        this.dataPost = dataPost;
        this.dataEntrega = dataEntrega;
        this.comentario = "";
        this.entregue = false;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDataPost() {
        return dataPost;
    }

    public String getDataEntrega() {
        return dataEntrega;
    }

    public String getComentario() {
        return comentario;
    }

    public boolean isEntregue() {
        return entregue;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public void entregar() {
        this.entregue = true;
    }
}