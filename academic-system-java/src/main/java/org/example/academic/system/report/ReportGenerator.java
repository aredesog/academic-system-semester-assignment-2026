package org.example.academic.system.report;

import org.example.academic.system.model.AcademicClass;
import org.example.academic.system.model.Assessment;

import java.util.List;

public class ReportGenerator {

    private static final double VALID_WEIGHT_TOLERANCE = 0.0001;

    // Gera relatório textual listando cada turma com suas avaliações, valor e peso
    public static String classAssessmentSummaryReport(List<AcademicClass> classes) {
        StringBuilder report = new StringBuilder();
        report.append("===== Relatorio de Avaliacoes por Turma =====").append(System.lineSeparator());

        if (classes.isEmpty()) {
            report.append(System.lineSeparator()).append("Nenhuma turma cadastrada.");
            return report.toString();
        }

        for (AcademicClass academicClass : classes) {
            report.append(System.lineSeparator())
                    .append("Turma: ").append(academicClass.getCode())
                    .append(" - ").append(academicClass.getTitle())
                    .append(System.lineSeparator())
                    .append("Avaliacoes:").append(System.lineSeparator());

            if (academicClass.getAssessments().isEmpty()) {
                report.append("Nenhuma avaliacao cadastrada.").append(System.lineSeparator());
                continue;
            }

            for (Assessment assessment : academicClass.getAssessments()) {
                report.append("* Tipo: ").append(formatAssessmentType(assessment))
                        .append(" | Valor: ").append(assessment.getValue())
                        .append(" | Peso: ").append(assessment.getWeight())
                        .append(System.lineSeparator());
            }
        }

        return report.toString();
    }

    // Gera relatório mostrando o peso total das avaliações de cada turma e se está válido (soma = 1.0)
    public static String assessmentWeightReport(List<AcademicClass> classes) {
        StringBuilder report = new StringBuilder();
        report.append("===== Relatorio de Peso das Avaliacoes =====").append(System.lineSeparator());

        if (classes.isEmpty()) {
            report.append(System.lineSeparator()).append("Nenhuma turma cadastrada.");
            return report.toString();
        }

        for (AcademicClass academicClass : classes) {
            double totalWeight = calculateTotalWeight(academicClass);
            String status = isValidWeight(totalWeight) ? "valido" : "invalido";

            report.append(System.lineSeparator())
                    .append("Turma: ").append(academicClass.getCode())
                    .append(" - ").append(academicClass.getTitle())
                    .append(System.lineSeparator())
                    .append("Peso total: ").append(totalWeight)
                    .append(System.lineSeparator())
                    .append("Status: ").append(status)
                    .append(System.lineSeparator());
        }

        return report.toString();
    }

    // Gera relatório informando qual tipo de persistência está ativo no momento
    public static String persistenceConfigurationReport(String formatName) {
        return "===== Relatorio de Configuracao de Persistencia =====" + System.lineSeparator()
                + "Tipo de persistencia ativo: " + formatName;
    }

    // Soma todos os pesos das avaliações de uma turma
    private static double calculateTotalWeight(AcademicClass academicClass) {
        return academicClass.getAssessments().stream()
                .mapToDouble(Assessment::getWeight)
                .sum();
    }

    // Verifica se a soma dos pesos está suficientemente próxima de 1.0 (tolerância de 0.0001)
    private static boolean isValidWeight(double totalWeight) {
        return Math.abs(totalWeight - 1.0) < VALID_WEIGHT_TOLERANCE;
    }

    // Traduz o tipo de avaliação do inglês para o português para exibição no relatório
    private static String formatAssessmentType(Assessment assessment) {
        return switch (assessment.getType()) {
            case "Exam" -> "Exame";
            case "Practical Assignment" -> "Atividade pratica";
            case "Seminar" -> "Seminario";
            case "Assignment" -> "Trabalho";
            default -> assessment.getType();
        };
    }
}
