package org.example.academic.system.model;

public class Assignment extends Assessment {
    // Cria uma avaliação do tipo Trabalho com valor e peso informados
    public Assignment(double value, double weight) {
        super(value, weight);
    }

    // Retorna o identificador do tipo desta avaliação
    @Override
    public String getType() {
        return "Assignment";
    }
}
