package com.javabootcamp.api.utils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;

import java.util.Optional;
import java.util.Set;

public class Utils {

    public static Optional<String> validarViolacoes (Object dto){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        jakarta.validation.Validator validator = factory.getValidator();
        Set<ConstraintViolation<Object>> violations = validator.validate(dto);

        if(!violations.isEmpty()){
            return Optional.of(violations.iterator().next().getMessage());
        }
        return Optional.empty();

    }
}
