package org.example.academic.system.controller;

import org.example.academic.system.model.AcademicClass;
import org.example.academic.system.model.AcademicSystem;
import org.example.academic.system.model.PersistenceType;
import org.example.academic.system.service.AssessmentService;
import org.example.academic.system.service.ClassService;
import org.example.academic.system.service.PersistenceService;
import org.example.academic.system.service.ReportService;
import java.util.List;

public class AcademicSystemController {

    private final ClassService classService;
    private final AssessmentService assessmentService;
    private final ReportService reportService;
    private final AcademicSystem academicSystem;
    private final PersistenceService persistenceService;


    public AcademicSystemController(ClassService classService,
                                    AssessmentService assessmentService,
                                    ReportService reportService,
                                    AcademicSystem academicSystem,
                                    PersistenceService persistenceService) {
        this.classService = classService;
        this.assessmentService = assessmentService;
        this.reportService = reportService;
        this.academicSystem = academicSystem;
        this.persistenceService = persistenceService;
    }

    public void registerClass(String code, String title) {
        classService.registerClass(code, title);
        persistenceService.save(academicSystem.getClasses());
    }

    public void registerAssessment(String classCode, String assessmentType, double value, double weight) {
        assessmentService.registerAssessment(classCode, assessmentType, value, weight);
        persistenceService.save(academicSystem.getClasses());
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

    public void configurePersistence(PersistenceType persistenceType){
        persistenceService.configurePersistenceType(persistenceType);
        academicSystem.replaceClasses(persistenceService.load());
    }

    public String getCurrentPersistenceFormat() {
        return persistenceService.getCurrentFormatName();
    }
}
