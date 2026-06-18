package org.example.academic.system.model;

public class PracticalAssignment extends Assessment {
    public PracticalAssignment(double value, double weight) {
        super(value, weight);
    }

    @Override
    public String getType() {
        return "Practical Assignment";
    }
}
