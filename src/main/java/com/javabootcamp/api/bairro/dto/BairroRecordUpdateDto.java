package com.javabootcamp.api.bairro.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BairroRecordUpdateDto(

        @NotNull(message = "O código do bairro não pode estar vazia ou ser nulo")
        Long codigoBairro,
        @NotNull(message = "O código do município não pode estar vazia ou ser nulo")
        Long codigoMunicipio,
        @NotBlank(message = "O nome não pode estar vazio ou ser nulo")
        String nome,
        @NotNull(message = "O status não pode estar vazio ou ser nulo")
        @Min(value = 1, message = "informe status 1 para ativo ou 2 para inativo")
        @Max(value = 2, message = "informe status 1 para ativo ou 2 para inativo")
        Integer status
) {
}
