package org.example.academic.system.service;

import org.example.academic.system.model.AcademicClass;
import org.example.academic.system.model.AcademicSystem;
import org.example.academic.system.model.Assessment;

public class ReportService {
    private static final double VALID_WEIGHT_TOLERANCE = 0.0001;

    private final AcademicSystem academicSystem;

    public ReportService(AcademicSystem academicSystem) {
        this.academicSystem = academicSystem;
    }

    public String generateClassAssessmentSummaryReport() {
        StringBuilder report = new StringBuilder();

        report.append("===== Relatorio de Avaliacoes por Turma =====").append(System.lineSeparator());

        if (academicSystem.getClasses().isEmpty()) {
            report.append(System.lineSeparator())
                    .append("Nenhuma turma cadastrada.");
            return report.toString();
        }

        for (AcademicClass academicClass : academicSystem.getClasses()) {
            report.append(System.lineSeparator())
                    .append("Turma: ")
                    .append(academicClass.getCode())
                    .append(" - ")
                    .append(academicClass.getTitle())
                    .append(System.lineSeparator())
                    .append("Avaliacoes:")
                    .append(System.lineSeparator());

            if (academicClass.getAssessments().isEmpty()) {
                report.append("Nenhuma avaliacao cadastrada.")
                        .append(System.lineSeparator());
                continue;
            }

            for (Assessment assessment : academicClass.getAssessments()) {
                report.append("* Tipo: ")
                        .append(formatAssessmentType(assessment))
                        .append(" | Valor: ")
                        .append(assessment.getValue())
                        .append(" | Peso: ")
                        .append(assessment.getWeight())
                        .append(System.lineSeparator());
            }
        }

        return report.toString();
    }

    public String generateAssessmentWeightReport() {
        StringBuilder report = new StringBuilder();

        report.append("===== Relatorio de Peso das Avaliacoes =====").append(System.lineSeparator());

        if (academicSystem.getClasses().isEmpty()) {
            report.append(System.lineSeparator())
                    .append("Nenhuma turma cadastrada.");
            return report.toString();
        }

        for (AcademicClass academicClass : academicSystem.getClasses()) {
            double totalWeight = calculateTotalWeight(academicClass);
            String status = isValidWeight(totalWeight) ? "valido" : "invalido";

            report.append(System.lineSeparator())
                    .append("Turma: ")
                    .append(academicClass.getCode())
                    .append(" - ")
                    .append(academicClass.getTitle())
                    .append(System.lineSeparator())
                    .append("Peso total: ")
                    .append(totalWeight)
                    .append(System.lineSeparator())
                    .append("Status: ")
                    .append(status)
                    .append(System.lineSeparator());
        }

        return report.toString();
    }

    private double calculateTotalWeight(AcademicClass academicClass) {
        return academicClass.getAssessments().stream()
                .mapToDouble(Assessment::getWeight)
                .sum();
    }

    private boolean isValidWeight(double totalWeight) {
        return Math.abs(totalWeight - 1.0) < VALID_WEIGHT_TOLERANCE;
    }

    private String formatAssessmentType(Assessment assessment) {
        return switch (assessment.getType()) {
            case "Exam" -> "Exame";
            case "Practical Assignment" -> "Atividade pratica";
            case "Seminar" -> "Seminario";
            case "Assignment" -> "Trabalho";
            default -> assessment.getType();
        };
    }
}
