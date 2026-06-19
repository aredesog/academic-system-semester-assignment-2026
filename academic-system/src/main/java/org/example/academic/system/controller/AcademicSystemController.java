package org.example.academic.system.controller;

import org.example.academic.system.model.AcademicClass;
import org.example.academic.system.model.AcademicSystem;
import org.example.academic.system.service.AssessmentService;
import org.example.academic.system.service.ClassService;
import org.example.academic.system.service.ReportService;
import org.example.academic.system.service.AssessmentService;
import org.example.academic.system.repository.*;

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
    private final PersistenceStrategy txtRepository = new TxtRepository();
    private final PersistenceStrategy jsonRepository = new JsonRepository();
    private final PersistenceStrategy xmlRepository = new XmlRepository();

    public AcademicSystemController(ClassService classService, AssessmentService assessmentService,
                                    AcademicSystem academicSystem, Object persistenceService) {
        this.classService = classService;
        this.assessmentService = assessmentService;
        this.reportService = reportService;
        this.academicSystem = academicSystem;
    }

    public void registerClass(String code, String title) {
        classService.registerClass(code, title);
        salvarEmTodosOsFormatos();
    }

    public void registerAssessment(String classCode, String assessmentType, double value, double weight) {
        assessmentService.registerAssessment(classCode, assessmentType, value, weight);
        salvarEmTodosOsFormatos();
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

    private void salvarEmTodosOsFormatos() {
        List<AcademicClass> listaAtual = academicSystem.getClasses();

        // Silencioso: Salva tudo sem printar nada na tela
        txtRepository.save(listaAtual);
        jsonRepository.save(listaAtual);
        xmlRepository.save(listaAtual);
    }

    public void changePersistenceFormat(String format) {}

    public String getCurrentFormatName() {
        return "TODOS (TXT, JSON, XML)";
    }
}