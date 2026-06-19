package org.example.academic.system.controller;

import org.example.academic.system.model.AcademicClass;
import org.example.academic.system.model.AcademicSystem;
import org.example.academic.system.model.PersistenceType;
import org.example.academic.system.model.Role;
import org.example.academic.system.security.AuthorizationService;
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
    private final AuthorizationService authorizationService;


    public AcademicSystemController(ClassService classService,
                                    AssessmentService assessmentService,
                                    ReportService reportService,
                                    AcademicSystem academicSystem,
                                    PersistenceService persistenceService,
                                    AuthorizationService authorizationService) {
        this.classService = classService;
        this.assessmentService = assessmentService;
        this.reportService = reportService;
        this.academicSystem = academicSystem;
        this.persistenceService = persistenceService;
        this.authorizationService = authorizationService;
    }

    public void registerClass(String code, String title) {
        authorizationService.authorize(Role.ADMIN);
        classService.registerClass(code, title);
        persistenceService.save(academicSystem.getClasses());
    }

    public void registerAssessment(String classCode, String assessmentType, double value, double weight) {
        authorizationService.authorize(Role.ADMIN, Role.PROFESSOR);
        assessmentService.registerAssessment(classCode, assessmentType, value, weight);
        persistenceService.save(academicSystem.getClasses());
    }

    public List<AcademicClass> listClasses() {
        authorizationService.authorize(Role.ADMIN, Role.PROFESSOR);
        return academicSystem.getClasses();
    }

    public String generateClassAssessmentSummaryReport() {
        authorizationService.authorize(Role.ADMIN, Role.PROFESSOR);
        return reportService.generateClassAssessmentSummaryReport();
    }

    public String generateAssessmentWeightReport() {
        authorizationService.authorize(Role.ADMIN, Role.PROFESSOR);
        return reportService.generateAssessmentWeightReport();
    }

    public void configurePersistence(PersistenceType persistenceType){
        authorizationService.authorize(Role.ADMIN);
        persistenceService.configurePersistenceType(persistenceType);
        academicSystem.replaceClasses(persistenceService.load());
    }

    public void saveAcademicData() {
        authorizationService.authorize(Role.ADMIN);
        persistenceService.save(academicSystem.getClasses());
    }

    public String generatePersistenceConfigurationReport() {
        authorizationService.authorize(Role.ADMIN);
        return persistenceService.generateConfigurationReport();
    }
}
