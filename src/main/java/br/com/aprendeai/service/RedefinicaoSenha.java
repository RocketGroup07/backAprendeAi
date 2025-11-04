package br.com.aprendeai.service;

import br.com.aprendeai.dtos.RedefinirSenhaDto;

public interface RedefinicaoSenha {
    void solicitarRedefinicao(String email);
    boolean validarCodigo(String email, String codigo);
	boolean redefinirSenha(RedefinirSenhaDto dto);
}
