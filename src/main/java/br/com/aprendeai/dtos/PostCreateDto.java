package br.com.aprendeai.dtos;

public record PostCreateDto(
		String titulo,
	    String conteudo,
	    boolean publico) {
}
