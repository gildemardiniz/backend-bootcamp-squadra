package com.javabootcamp.api.pessoa.dto;

import com.javabootcamp.api.endereco.dto.EnderecoRecordUpdateDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PessoaRecordUpdateDto(
        @NotNull(message = "O código da pessoa  não pode estar vazio ou ser nulo")
        Long codigoPessoa,
        @NotBlank(message = "O nome da rua não pode estar vazio ou ser nulo")
        String nome,
        @NotBlank(message = "O sobrenome não pode estar vazio ou ser nulo")
        String sobrenome,
        @NotNull(message = "A idade não pode estar vazio ou ser nulo")
        Integer idade,
        @NotNull(message = "O login não pode estar vazio ou ser nulo")
        String login,
        @NotNull(message = "A senha não pode estar vazio ou ser nulo")
        String senha,
        @NotNull(message = "O status não pode estar vazio ou ser nulo")
        @Min(value = 1, message = "informe status 1 para ativo ou 2 para inativo")
        @Max(value = 2, message = "informe status 1 para ativo ou 2 para inativo")
        Integer status,

        @Valid
        @NotNull(message = "O endereço não pode ser nulo")
        List<EnderecoRecordUpdateDto> enderecos


) {
}
