package br.com.aprendeai.dtos;

import lombok.Data;

@Data
public class PostRequestDTO {

	private String titulo;
	private String conteudo;
	private boolean publico;
	private Long turmaId;
	private Long usuarioId;
}
