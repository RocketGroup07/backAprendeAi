package br.com.aprendeai.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RedefinicaoRequestDto(
    @NotBlank @Email String email
) {}
