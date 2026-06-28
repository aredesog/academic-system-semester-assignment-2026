package org.example.academic.system.service;

import org.example.academic.system.model.AcademicSystem;
import org.example.academic.system.report.ReportGenerator;
import org.example.academic.system.security.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReportService {

    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

    private final AcademicSystem academicSystem;

    public ReportService(AcademicSystem academicSystem) {
        this.academicSystem = academicSystem;
    }

    public String generateClassAssessmentSummaryReport() {
        logger.info("Generating class assessment summary report for role: {}", getCurrentUserRole());
        String report = ReportGenerator.classAssessmentSummaryReport(academicSystem.getClasses());
        logger.info("Class assessment summary report generated successfully");
        return report;
    }

    public String generateAssessmentWeightReport() {
        logger.info("Generating assessment weight report for role: {}", getCurrentUserRole());
        String report = ReportGenerator.assessmentWeightReport(academicSystem.getClasses());
        logger.info("Assessment weight report generated successfully");
        return report;
    }

    private String getCurrentUserRole() {
        var user = Session.getInstance().getAuthenticatedUser();
        return user != null ? user.getRole().toString() : "UNKNOWN";
    }
}
