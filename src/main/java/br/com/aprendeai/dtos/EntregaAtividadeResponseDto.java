package br.com.aprendeai.dtos;

import java.util.List;

import br.com.aprendeai.enums.StatusAtividade;

public record EntregaAtividadeResponseDto(
			Long id,
			String titulo,
			boolean entregue,
			String respostaTexto,
			StatusAtividade status,
			List<String> nomesArquivoEntrega
			) {
	
}
