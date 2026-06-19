package org.example.academic.system.model;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.example.academic.system.validation.DomainValidator;

public abstract class Assessment {
    @PositiveOrZero(message = "O valor da avaliacao nao pode ser negativo.")
    private final double value;

    @Positive(message = "O peso da avaliacao deve ser maior que zero.")
    private final double weight;

    protected Assessment(double value, double weight) {
        this.value = value;
        this.weight = weight;

        DomainValidator.validate(this);
    }

    public double getValue() {
        return value;
    }

    public double getWeight() {
        return weight;
    }

    public abstract String getType();

    @Override
    public String toString() {
        return getType() + " - valor: " + value + ", peso: " + weight;
    }
}
