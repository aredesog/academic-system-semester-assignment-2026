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


    // Injeta todos os serviços necessários para o funcionamento do controller
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

    // Cadastra uma nova turma (somente ADMIN), salvando os dados logo após
    public void registerClass(String code, String title) {
        authorizationService.authorize(Role.ADMIN);
        classService.registerClass(code, title);
        persistenceService.save(academicSystem.getClasses());
    }

    // Cadastra uma avaliação em uma turma (ADMIN ou PROFESSOR), salvando os dados logo após
    public void registerAssessment(String classCode, String assessmentType, double value, double weight) {
        authorizationService.authorize(Role.ADMIN, Role.PROFESSOR);
        assessmentService.registerAssessment(classCode, assessmentType, value, weight);
        persistenceService.save(academicSystem.getClasses());
    }

    // Retorna a lista de turmas cadastradas (ADMIN ou PROFESSOR)
    public List<AcademicClass> listClasses() {
        authorizationService.authorize(Role.ADMIN, Role.PROFESSOR);
        return academicSystem.getClasses();
    }

    // Gera relatório de avaliações agrupadas por turma (ADMIN ou PROFESSOR)
    public String generateClassAssessmentSummaryReport() {
        authorizationService.authorize(Role.ADMIN, Role.PROFESSOR);
        return reportService.generateClassAssessmentSummaryReport();
    }

    // Gera relatório de validação do peso total das avaliações por turma (ADMIN ou PROFESSOR)
    public String generateAssessmentWeightReport() {
        authorizationService.authorize(Role.ADMIN, Role.PROFESSOR);
        return reportService.generateAssessmentWeightReport();
    }

    // Muda o tipo de persistência ativo e recarrega os dados do novo formato (somente ADMIN)
    public void configurePersistence(PersistenceType persistenceType){
        authorizationService.authorize(Role.ADMIN);
        persistenceService.configurePersistenceType(persistenceType);
        academicSystem.replaceClasses(persistenceService.load());
    }

    // Força o salvamento dos dados acadêmicos no formato atual (somente ADMIN)
    public void saveAcademicData() {
        authorizationService.authorize(Role.ADMIN);
        persistenceService.save(academicSystem.getClasses());
    }

    // Gera relatório informando qual tipo de persistência está configurado (somente ADMIN)
    public String generatePersistenceConfigurationReport() {
        authorizationService.authorize(Role.ADMIN);
        return persistenceService.generateConfigurationReport();
    }
}
