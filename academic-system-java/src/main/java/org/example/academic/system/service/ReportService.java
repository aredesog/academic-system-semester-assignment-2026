package org.example.academic.system.service;

import org.example.academic.system.model.AcademicSystem;
import org.example.academic.system.report.ReportGenerator;
import org.example.academic.system.security.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReportService {

    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

    private final AcademicSystem academicSystem;

    // Injeta o modelo central para acessar a lista de turmas na geração dos relatórios
    public ReportService(AcademicSystem academicSystem) {
        this.academicSystem = academicSystem;
    }

    // Gera relatório listando todas as avaliações cadastradas por turma
    public String generateClassAssessmentSummaryReport() {
        logger.info("Generating class assessment summary report for role: {}", getCurrentUserRole());
        String report = ReportGenerator.classAssessmentSummaryReport(academicSystem.getClasses());
        logger.info("Class assessment summary report generated successfully");
        return report;
    }

    // Gera relatório mostrando o peso total das avaliações por turma e se está válido (soma = 1.0)
    public String generateAssessmentWeightReport() {
        logger.info("Generating assessment weight report for role: {}", getCurrentUserRole());
        String report = ReportGenerator.assessmentWeightReport(academicSystem.getClasses());
        logger.info("Assessment weight report generated successfully");
        return report;
    }

    // Obtém o papel do usuário autenticado na sessão para fins de log
    private String getCurrentUserRole() {
        var user = Session.getInstance().getAuthenticatedUser();
        return user != null ? user.getRole().toString() : "UNKNOWN";
    }
}
