package org.example.academic.system.controller;

import org.example.academic.system.model.AcademicClass;
import org.example.academic.system.model.AcademicSystem;
import org.example.academic.system.service.AssessmentService;
import org.example.academic.system.service.ClassService;
import org.example.academic.system.service.ReportService;

import java.util.List;

public class AcademicSystemController {
    private final ClassService classService;
    private final AssessmentService assessmentService;
    private final ReportService reportService;
    private final AcademicSystem academicSystem;

    public AcademicSystemController(
            ClassService classService,
            AssessmentService assessmentService,
            ReportService reportService,
            AcademicSystem academicSystem
    ) {
        this.classService = classService;
        this.assessmentService = assessmentService;
        this.reportService = reportService;
        this.academicSystem = academicSystem;
    }

    public void registerClass(String code, String title) {
        classService.registerClass(code, title);
    }

    public void registerAssessment(String classCode, String assessmentType, double value, double weight) {
        assessmentService.registerAssessment(classCode, assessmentType, value, weight);
    }

    public List<AcademicClass> listClasses() {
        return academicSystem.getClasses();
    }

    public String generateClassAssessmentSummaryReport() {
        return reportService.generateClassAssessmentSummaryReport();
    }

    public String generateAssessmentWeightReport() {
        return reportService.generateAssessmentWeightReport();
    }
}
