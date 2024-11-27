package com.javabootcamp.api.error;

public record ErrorDto(
        String mensagem,
        Integer status
) {

}

