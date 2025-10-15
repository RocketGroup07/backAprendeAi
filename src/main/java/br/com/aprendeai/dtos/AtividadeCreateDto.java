package br.com.aprendeai.dtos;

import java.time.LocalDateTime;

import lombok.Data;
@Data
public class AtividadeCreateDto{
	

		private String titulo;
		private LocalDateTime dataEntrega;
		private String conteudo; 

}
