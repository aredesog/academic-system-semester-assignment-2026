package org.example.academic.system.model;

public class Seminar extends Assessment {
    public Seminar(double value, double weight) {
        super(value, weight);
    }

    @Override
    public String getType() {
        return "Seminar";
    }
}
