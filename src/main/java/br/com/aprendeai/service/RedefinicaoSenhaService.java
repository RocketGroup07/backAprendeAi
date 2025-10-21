package br.com.aprendeai.service;

public interface RedefinicaoSenhaService {

    void solicitarRedefinicao(String email, String codigo);

    boolean validarCodigo(String email, String codigo);

}
