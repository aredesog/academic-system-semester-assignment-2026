package org.example.academic.system.model;

public class PracticalAssignment extends Assessment {
    // Cria uma avaliação do tipo Atividade Prática com valor e peso informados
    public PracticalAssignment(double value, double weight) {
        super(value, weight);
    }

    // Retorna o identificador do tipo desta avaliação
    @Override
    public String getType() {
        return "Practical Assignment";
    }
}
