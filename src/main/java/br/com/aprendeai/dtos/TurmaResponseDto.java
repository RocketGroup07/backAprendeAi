package br.com.aprendeai.dtos;

import java.time.LocalDateTime;
import java.util.Set;

public record TurmaResponseDto(
		Long id,
        String nome,
        int limiteAlunos,
        Integer cargaHorariaTotal,
        String codigo,
        UsuarioResponseDto professor,
        Set<UsuarioResponseDto> alunos,
        LocalDateTime criadoEm
        ) {

}
