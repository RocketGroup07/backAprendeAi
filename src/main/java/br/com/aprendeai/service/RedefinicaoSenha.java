package br.com.aprendeai.service;

import org.springframework.stereotype.Service;

@Service
public interface RedefinicaoSenha {
	
	void solicitarRedefinicao(String email, String codigo);

    boolean validarCodigo(String email, String codigo);

}
