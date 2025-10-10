package br.com.aprendeai.dtos;

public record FrequenciaDTO(
        Long alunoId,
        String nomeAluno,
        Integer horasPresenteTotal,
        Integer cargaHorariaTotal,
        double percentualPresenca
) {}
