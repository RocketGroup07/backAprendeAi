package br.com.aprendeai.dtos;

import java.time.LocalDate;

public record DiaAulaResponseDTO(
    Long id,
    LocalDate dataAula,
    String conteudo,
    Integer horasMaximas,
    Integer horasTotais,
    Long turmaId
) {}
