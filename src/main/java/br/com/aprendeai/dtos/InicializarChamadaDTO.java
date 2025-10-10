package br.com.aprendeai.dtos;

import java.time.LocalDate;

public record InicializarChamadaDTO(
        Long turmaId,
        LocalDate dataAula,
        Integer horasMaximas
) {}
