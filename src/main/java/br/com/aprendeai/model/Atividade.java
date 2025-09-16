package br.com.aprendeai.model;

/**
 * Representa uma atividade com título, datas, comentário e status de entrega.
 */
public class Atividade {
    private final String titulo;
    private final String dataPost;
    private final String dataEntrega;
    private String comentario;
    private boolean entregue;

    public Atividade(String titulo, String dataPost, String dataEntrega) {
        if (titulo == null || titulo.isBlank()) throw new IllegalArgumentException("Título não pode ser vazio");
        if (dataPost == null || dataPost.isBlank()) throw new IllegalArgumentException("Data do post não pode ser vazia");
        if (dataEntrega == null || dataEntrega.isBlank()) throw new IllegalArgumentException("Data de entrega não pode ser vazia");
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

    /**
     * Define o comentário da atividade.
     * @param comentario Comentário do usuário.
     */
    public void setComentario(String comentario) {
        if (comentario == null) throw new IllegalArgumentException("Comentário não pode ser nulo");
        this.comentario = comentario;
    }

    /**
     * Marca a atividade como entregue.
     */
    public void entregar() {
        this.entregue = true;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Atividade)) return false;
        Atividade other = (Atividade) obj;
        return titulo.equalsIgnoreCase(other.titulo);
    }

    @Override
    public int hashCode() {
        return titulo.toLowerCase().hashCode();
    }
}