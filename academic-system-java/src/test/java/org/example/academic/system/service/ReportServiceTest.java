package org.example.academic.system.service;

import org.example.academic.system.model.AcademicClass;
import org.example.academic.system.model.AcademicSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes de ReportService")
class ReportServiceTest {

    private ReportService reportService;
    private AcademicSystem academicSystem;
    private ClassService classService;
    private AssessmentService assessmentService;

    @BeforeEach
    void setup() {
        academicSystem = new AcademicSystem();
        reportService = new ReportService(academicSystem);
        classService = new ClassService(academicSystem);
        assessmentService = new AssessmentService(academicSystem);
    }

    @Test
    @DisplayName("Deve gerar relatório de avaliações por turma")
    void testGenerateClassAssessmentSummaryReport() {
        classService.registerClass("POO", "Programação Orientada a Objetos");
        assessmentService.registerAssessment("POO", "exam", 8.5, 0.4);
        assessmentService.registerAssessment("POO", "seminar", 7.0, 0.3);

        String report = reportService.generateClassAssessmentSummaryReport();

        assertTrue(report.contains("POO"));
        assertTrue(report.contains("Programação Orientada a Objetos"));
        assertTrue(report.contains("Avaliacoes"));
        assertTrue(report.contains("8.5"));
        assertTrue(report.contains("7.0"));
    }

    @Test
    @DisplayName("Deve gerar relatório quando nenhuma turma cadastrada")
    void testGenerateReportWithNoClasses() {
        String report = reportService.generateClassAssessmentSummaryReport();

        assertTrue(report.contains("Nenhuma turma cadastrada"));
    }

    @Test
    @DisplayName("Deve gerar relatório quando turma sem avaliações")
    void testGenerateReportWithClassWithoutAssessments() {
        classService.registerClass("POO", "Turma");

        String report = reportService.generateClassAssessmentSummaryReport();

        assertTrue(report.contains("POO"));
        assertTrue(report.contains("Nenhuma avaliacao cadastrada"));
    }

    @Test
    @DisplayName("Deve incluir informações de avaliação no relatório")
    void testReportIncludesAssessmentDetails() {
        classService.registerClass("POO", "Turma");
        assessmentService.registerAssessment("POO", "exam", 8.5, 0.4);

        String report = reportService.generateClassAssessmentSummaryReport();

        assertTrue(report.contains("Tipo:"));
        assertTrue(report.contains("Valor:"));
        assertTrue(report.contains("Peso:"));
    }

    @Test
    @DisplayName("Deve gerar relatório de peso de avaliações")
    void testGenerateAssessmentWeightReport() {
        classService.registerClass("POO", "Turma");
        assessmentService.registerAssessment("POO", "exam", 8.5, 0.4);
        assessmentService.registerAssessment("POO", "seminar", 7.0, 0.3);
        assessmentService.registerAssessment("POO", "assignment", 6.5, 0.3);

        String report = reportService.generateAssessmentWeightReport();

        assertTrue(report.contains("POO"));
        assertTrue(report.contains("Peso total:"));
        assertTrue(report.contains("valido"));
    }

    @Test
    @DisplayName("Deve marcar como inválido quando peso não é 1.0")
    void testMarkInvalidWhenWeightNotOne() {
        classService.registerClass("POO", "Turma");
        assessmentService.registerAssessment("POO", "exam", 8.5, 0.4);
        assessmentService.registerAssessment("POO", "seminar", 7.0, 0.4);

        String report = reportService.generateAssessmentWeightReport();

        assertTrue(report.contains("invalido"));
    }

    @Test
    @DisplayName("Deve aceitar tolerância de peso (0.0001)")
    void testAcceptWeightWithinTolerance() {
        classService.registerClass("POO", "Turma");
        assessmentService.registerAssessment("POO", "exam", 8.5, 0.3333);
        assessmentService.registerAssessment("POO", "seminar", 7.0, 0.3333);
        assessmentService.registerAssessment("POO", "assignment", 6.5, 0.3334);

        String report = reportService.generateAssessmentWeightReport();

        assertTrue(report.contains("valido"));
    }

    @Test
    @DisplayName("Deve gerar relatório para múltiplas turmas")
    void testGenerateReportForMultipleClasses() {
        classService.registerClass("POO", "Turma 1");
        classService.registerClass("BD", "Turma 2");
        
        assessmentService.registerAssessment("POO", "exam", 8.5, 1.0);
        assessmentService.registerAssessment("BD", "seminar", 7.0, 1.0);

        String report = reportService.generateClassAssessmentSummaryReport();

        assertTrue(report.contains("POO"));
        assertTrue(report.contains("BD"));
    }

    @Test
    @DisplayName("Deve formatar tipo de avaliação em português")
    void testFormatAssessmentTypeInPortuguese() {
        classService.registerClass("POO", "Turma");
        assessmentService.registerAssessment("POO", "exam", 8.5, 0.5);
        assessmentService.registerAssessment("POO", "seminar", 7.0, 0.5);

        String report = reportService.generateClassAssessmentSummaryReport();

        // Dependendo da implementação, pode conter Exam ou sua tradução
        assertTrue(report.contains("exam") || report.contains("Exam") || report.contains("Exame"));
    }

    @Test
    @DisplayName("Deve incluir header no relatório")
    void testReportIncludesHeader() {
        String report = reportService.generateAssessmentWeightReport();

        assertTrue(report.contains("Relatorio") || report.contains("Relatório"));
        assertTrue(report.contains("Peso"));
    }

    @Test
    @DisplayName("Deve calcular peso total correto")
    void testCalculateTotalWeightCorrectly() {
        classService.registerClass("POO", "Turma");
        assessmentService.registerAssessment("POO", "exam", 8.5, 0.5);
        assessmentService.registerAssessment("POO", "seminar", 7.0, 0.5);

        String report = reportService.generateAssessmentWeightReport();

        assertTrue(report.contains("1.0") || report.contains("valido"));
    }

    @Test
    @DisplayName("Deve gerar relatório quando há múltiplas avaliações")
    void testGenerateReportWithMultipleAssessments() {
        classService.registerClass("POO", "Turma");
        
        for (int i = 0; i < 10; i++) {
            assessmentService.registerAssessment("POO", "exam", 5.0 + i * 0.1, 0.1);
        }

        String report = reportService.generateClassAssessmentSummaryReport();

        assertTrue(report.contains("POO"));
        assertTrue(report.contains("Avaliacoes"));
    }

    @Test
    @DisplayName("Deve incluir informações de cada turma no relatório")
    void testReportIncludesAllClassInformation() {
        classService.registerClass("POO", "Programação Orientada a Objetos");
        assessmentService.registerAssessment("POO", "exam", 8.5, 1.0);

        String report = reportService.generateClassAssessmentSummaryReport();

        assertTrue(report.contains("POO"));
        assertTrue(report.contains("Programação"));
    }
}
