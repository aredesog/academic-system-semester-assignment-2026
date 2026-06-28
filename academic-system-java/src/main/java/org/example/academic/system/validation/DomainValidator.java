package org.example.academic.system.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.example.academic.system.exception.AcademicSystemException;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import java.util.Set;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DomainValidator {
    private static final Validator VALIDATOR = createValidator();

    // Construtor privado para impedir instanciação — classe utilitária com apenas métodos estáticos
    private DomainValidator() {
    }

    // Valida um objeto usando as anotações Bean Validation (@NotBlank, @Positive, etc.) e lança exceção com todas as violações encontradas
    public static <T> void validate(T object) {
        Set<ConstraintViolation<T>> violations = VALIDATOR.validate(object);

        if (!violations.isEmpty()) {
            String message = violations.stream()
                    .sorted(Comparator.comparing(violation -> violation.getPropertyPath().toString()))
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(System.lineSeparator() + "- ", "- ", ""));

            throw new AcademicSystemException(message);
        }
    }

    // Cria e configura o Validator do Hibernate sem EL para evitar dependência de Jakarta EL
    private static Validator createValidator() {
        Logger.getLogger("org.hibernate.validator").setLevel(Level.OFF);

        ValidatorFactory factory = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory();

        return factory.getValidator();
    }
}
