package com.javabootcamp.api.endereco.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EnderecoRecordDto(

        @NotNull(message = "O código do bairro  não pode estar vazio ou ser nulo")
        Long codigoBairro,
        @NotBlank(message = "O Nome da rua não pode estar vazio ou ser nulo")
        String nomeRua,
        @NotBlank(message = "O número não pode estar vazio ou ser nulo")
        String numero,
        @NotBlank(message = "O complemento não pode estar vazio ou ser nulo")
        String complemento,
        @NotBlank(message = "O CEP não pode estar vazio ou ser nulo")
        String cep,
        @NotNull(message = "O código da pessoa não pode estar vazio ou ser nulo")
        Long codigoPessoa
) {
}
