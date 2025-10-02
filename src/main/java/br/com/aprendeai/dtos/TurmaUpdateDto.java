package br.com.aprendeai.dtos;

public record TurmaUpdateDto(
		String nome,
        Integer limiteAlunos,
        Long professorId
       ) {

}
