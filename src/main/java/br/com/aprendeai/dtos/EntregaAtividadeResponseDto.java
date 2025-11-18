package br.com.aprendeai.dtos;

import java.util.List;

import br.com.aprendeai.enums.StatusAtividade;

public record EntregaAtividadeResponseDto(
			Long id,
			String titulo,
			boolean entregue,
			StatusAtividade status,
			List<String> nomesArquivoEntrega
			) {
	
}
