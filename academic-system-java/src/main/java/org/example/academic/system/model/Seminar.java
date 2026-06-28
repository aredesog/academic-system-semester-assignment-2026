package org.example.academic.system.model;

public class Seminar extends Assessment {
    // Cria uma avaliação do tipo Seminário com valor e peso informados
    public Seminar(double value, double weight) {
        super(value, weight);
    }

    // Retorna o identificador do tipo desta avaliação
    @Override
    public String getType() {
        return "Seminar";
    }
}
