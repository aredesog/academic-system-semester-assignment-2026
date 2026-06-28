package org.example.academic.system.model;

public class Exam extends Assessment {
    public Exam(double value, double weight) {
        super(value, weight);
    }

    @Override
    public String getType() {
        return "Exam";
    }
}
