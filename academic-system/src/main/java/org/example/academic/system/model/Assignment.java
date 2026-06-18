package org.example.academic.system.model;

public class Assignment extends Assessment {
    public Assignment(double value, double weight) {
        super(value, weight);
    }

    @Override
    public String getType() {
        return "Assignment";
    }
}
