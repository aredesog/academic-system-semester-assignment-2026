package org.example.academic.system;

import org.example.academic.system.controller.AcademicSystemController;
import org.example.academic.system.controller.AuthenticationController;
import org.example.academic.system.model.AcademicClass;
import org.example.academic.system.model.AcademicSystem;
import org.example.academic.system.repository.TxtUserRepository;
import org.example.academic.system.security.AuthenticationService;
import org.example.academic.system.security.AuthorizationService;
import org.example.academic.system.service.AssessmentService;
import org.example.academic.system.service.ClassService;
import org.example.academic.system.service.ReportService;
import org.example.academic.system.service.PersistenceService;
import org.example.academic.system.view.ConsoleMenu;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        AcademicSystem academicSystem = new AcademicSystem();
        ClassService classService = new ClassService(academicSystem);
        AssessmentService assessmentService = new AssessmentService(academicSystem);
        ReportService reportService = new ReportService(academicSystem);
        PersistenceService persistenceService = new PersistenceService();
        AuthenticationService authenticationService = new AuthenticationService(new TxtUserRepository());
        AuthenticationController authenticationController = new AuthenticationController(authenticationService);
        AuthorizationService authorizationService = new AuthorizationService();

        // ====================================================================
        // CARGA INICIAL DE DADOS CORRIGIDA (TURMAS + AVALIAÇÕES)
        // ====================================================================
        try {
            List<AcademicClass> turmasSalvas = persistenceService.load();
            academicSystem.replaceClasses(turmasSalvas);
            System.out.println("[Persistência] Histórico de turmas e avaliações carregado com sucesso!");
        } catch (Exception e) {
            System.out.println("[Persistência] Nenhum dado inicial carregado ou arquivo vazio/novo.");
        }
        // ====================================================================

        AcademicSystemController controller = new AcademicSystemController(
                classService,
                assessmentService,
                reportService,
                academicSystem,
                persistenceService,
                authorizationService
        );

        ConsoleMenu menu = new ConsoleMenu(controller, authenticationController);
        menu.start();
    }
}
