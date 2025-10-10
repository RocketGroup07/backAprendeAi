package br.com.aprendeai.dtos;

public record PresencaDTO(
        Long id,
        Long alunoId,
        String nomeAluno,
        Integer horasPresente
) {}
