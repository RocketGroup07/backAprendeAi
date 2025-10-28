package br.com.aprendeai.dtos;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtividadeCreateDto{

		private String titulo;
		private LocalDateTime dataEntrega;
		 @Size(max = 5000)
		private String conteudo; 

}
