package br.com.aprendeai.dtos;

import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

public record DiaAulaCreateDTO(
    @NotNull(message = "A data da aula é obrigatória")
    LocalDate dataAula,

    @NotBlank(message = "O conteúdo é obrigatório")
    String conteudo,

    @NotNull(message = "O ID da turma é obrigatório")
    Long turmaId,

    @NotNull(message = "Horas máximas são obrigatórias")
    Integer horasMaximas,

    @NotNull(message = "Horas totais são obrigatórias")
    Integer horasTotais
) {}
