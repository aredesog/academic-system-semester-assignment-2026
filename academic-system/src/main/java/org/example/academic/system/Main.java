package org.example.academic.system;

import org.example.academic.system.controller.AcademicSystemController;
import org.example.academic.system.model.AcademicClass;
import org.example.academic.system.model.Assessment;
import org.example.academic.system.model.AcademicSystem;
import org.example.academic.system.service.AssessmentService;
import org.example.academic.system.service.ClassService;
import org.example.academic.system.service.PersistenceService;
import org.example.academic.system.view.ConsoleMenu;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        AcademicSystem academicSystem = new AcademicSystem();
        ClassService classService = new ClassService(academicSystem);
        AssessmentService assessmentService = new AssessmentService(academicSystem);
        PersistenceService persistenceService = new PersistenceService();

        // ====================================================================
        // CARGA INICIAL DE DADOS CORRIGIDA (TURMAS + AVALIAÇÕES)
        // ====================================================================
        try {
            List<AcademicClass> turmasSalvas = persistenceService.load();
            for (AcademicClass turma : turmasSalvas) {
                classService.registerClass(turma.getCode(), turma.getTitle());

                // 2. Se a turma do arquivo tiver avaliações cadastradas, insere cada uma delas
                if (turma.getAssessments() != null && !turma.getAssessments().isEmpty()) {
                    for (Assessment avaliacao : turma.getAssessments()) {
                        assessmentService.registerAssessment(
                                turma.getCode(),
                                avaliacao.getType(),
                                avaliacao.getValue(),
                                avaliacao.getWeight()
                        );
                    }
                }
            }
            System.out.println("[Persistência] Histórico de turmas e avaliações carregado com sucesso!");
        } catch (Exception e) {
            System.out.println("[Persistência] Nenhum dado inicial carregado ou arquivo vazio/novo.");
        }
        // ====================================================================

        AcademicSystemController controller = new AcademicSystemController(
                classService,
                assessmentService,
                academicSystem,
                persistenceService
        );

        ConsoleMenu menu = new ConsoleMenu(controller);
        menu.start();
    }
}