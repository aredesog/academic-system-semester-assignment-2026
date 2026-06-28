package org.example.academic.system.model;

public class Exam extends Assessment {
    // Cria uma avaliação do tipo Exame com valor e peso informados
    public Exam(double value, double weight) {
        super(value, weight);
    }

    // Retorna o identificador do tipo desta avaliação
    @Override
    public String getType() {
        return "Exam";
    }
}
