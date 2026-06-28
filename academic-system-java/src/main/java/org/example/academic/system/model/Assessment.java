package org.example.academic.system.model;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.example.academic.system.validation.DomainValidator;

public abstract class Assessment {
    @PositiveOrZero(message = "O valor da avaliacao nao pode ser negativo.")
    private final double value;

    @Positive(message = "O peso da avaliacao deve ser maior que zero.")
    private final double weight;

    // Inicializa a avaliação com valor e peso, validando as constraints da classe
    protected Assessment(double value, double weight) {
        this.value = value;
        this.weight = weight;

        DomainValidator.validate(this);
    }

    // Retorna a nota da avaliação
    public double getValue() {
        return value;
    }

    // Retorna o peso da avaliação no cálculo final
    public double getWeight() {
        return weight;
    }

    // Método abstrato que cada subclasse implementa para informar seu tipo (Exam, Assignment, etc.)
    public abstract String getType();

    // Representação textual mostrando o tipo, valor e peso da avaliação
    @Override
    public String toString() {
        return getType() + " - valor: " + value + ", peso: " + weight;
    }
}
