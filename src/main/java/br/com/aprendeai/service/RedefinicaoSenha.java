package br.com.aprendeai.service;

public interface RedefinicaoSenha {
    void solicitarRedefinicao(String email);
    boolean validarCodigo(String email, String codigo);
    boolean redefinirSenha(String email, String novaSenha);
}
