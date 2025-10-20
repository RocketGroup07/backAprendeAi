package br.com.aprendeai.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ValidarCodigoDto(
    @NotBlank @Email String email,
    @NotBlank String codigo
) {}
