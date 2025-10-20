package br.com.aprendeai.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtividadeCreateDto{
	

		private String titulo;
		private LocalDateTime dataEntrega;
		private String conteudo; 

}
