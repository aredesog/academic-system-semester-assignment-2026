package org.example.academic.system.model;

import org.example.academic.system.exception.AcademicSystemException;

public abstract class Assessment {
    private final double value;
    private final double weight;

    protected Assessment(double value, double weight) {
        if (value < 0) {
            throw new AcademicSystemException("O valor da avaliacao nao pode ser negativo.");
        }

        if (weight <= 0) {
            throw new AcademicSystemException("O peso da avaliacao deve ser maior que zero.");
        }

        this.value = value;
        this.weight = weight;
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
