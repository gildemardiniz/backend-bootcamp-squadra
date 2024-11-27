package com.javabootcamp.api.municipio.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MunicipioRecordUpdateDto(

        @NotNull(message = "O código da município não pode estar vazia ou ser nulo")
        Long codigoMunicipio,
        @NotNull (message = "O código da Uf não pode estar vazia ou ser nulo")
        Long codigoUf,
        @NotBlank (message = "O nome não pode estar vazio ou ser nulo")
        String nome,
        @NotNull(message = "O status não pode estar vazio ou ser nulo")
        @Min(value = 1 , message = "informe status 1 para ativo ou 2 para inativo")
        @Max(value = 2 , message = "informe status 1 para ativo ou 2 para inativo")
        Integer status
) {
}
