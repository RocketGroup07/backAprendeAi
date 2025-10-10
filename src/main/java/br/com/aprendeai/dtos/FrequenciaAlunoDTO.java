package br.com.aprendeai.dtos;

public record FrequenciaAlunoDTO(
        Long alunoId,
        String nomeAluno,
        Integer horasPresenteTotal,
        Integer cargaHorariaTotal,
        double percentualPresenca,
        double percentualFalta
) {}
