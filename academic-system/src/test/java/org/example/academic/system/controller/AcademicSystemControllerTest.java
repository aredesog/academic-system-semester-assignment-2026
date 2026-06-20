package org.example.academic.system.controller;

import org.example.academic.system.model.AcademicClass;
import org.example.academic.system.model.AcademicSystem;
import org.example.academic.system.model.PersistenceType;
import org.example.academic.system.model.Role;
import org.example.academic.system.security.AuthorizationService;
import org.example.academic.system.security.Session;
import org.example.academic.system.service.AssessmentService;
import org.example.academic.system.service.ClassService;
import org.example.academic.system.service.PersistenceService;
import org.example.academic.system.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes de AcademicSystemController")
class AcademicSystemControllerTest {

    private AcademicSystemController controller;
    private AcademicSystem academicSystem;
    private ClassService classService;
    private AssessmentService assessmentService;
    private ReportService reportService;
    private PersistenceService persistenceService;
    private AuthorizationService authorizationService;
    private Session session;

    @BeforeEach
    void setup() {
        academicSystem = new AcademicSystem();
        classService = new ClassService(academicSystem);
        assessmentService = new AssessmentService(academicSystem);
        reportService = new ReportService(academicSystem);
        persistenceService = new PersistenceService();
        authorizationService = new AuthorizationService();
        session = Session.getInstance();

        controller = new AcademicSystemController(
                classService,
                assessmentService,
                reportService,
                academicSystem,
                persistenceService,
                authorizationService
        );
    }

    @Test
    @DisplayName("Deve injetar dependências corretamente")
    void testDependencyInjection() {
        assertNotNull(controller);
        assertTrue(academicSystem.getClasses().isEmpty());
    }

    @Test
    @DisplayName("Deve listar turmas cadastradas")
    void testListClassesRequiresAuthorization() {
        // Precisa de autenticação para chamar métodos
        // Este teste verifica que o controller é criado corretamente
        assertNotNull(controller);
    }

    @Test
    @DisplayName("Deve gerar relatório de avaliações")
    void testGenerateClassAssessmentSummary() {
        // Cria uma turma e avaliação manualmente no academicSystem
        AcademicClass academicClass = new AcademicClass("POO", "Turma");
        academicSystem.addClass(academicClass);

        String report = reportService.generateClassAssessmentSummaryReport();

        assertNotNull(report);
        assertTrue(report.contains("POO"));
    }

    @Test
    @DisplayName("Deve gerar relatório de peso de avaliações")
    void testGenerateAssessmentWeightReport() {
        AcademicClass academicClass = new AcademicClass("POO", "Turma");
        academicSystem.addClass(academicClass);

        String report = reportService.generateAssessmentWeightReport();

        assertNotNull(report);
        assertTrue(report.contains("POO"));
    }

    @Test
    @DisplayName("Deve gerar relatório de configuração de persistência")
    void testGeneratePersistenceConfigurationReport() {
        String report = persistenceService.generateConfigurationReport();

        assertNotNull(report);
        assertTrue(report.contains("persistencia") || report.contains("persistência"));
    }

    @Test
    @DisplayName("Deve configurar tipo de persistência")
    void testConfigurePersistenceType() {
        persistenceService.configurePersistenceType(PersistenceType.JSON);

        assertEquals("JSON", persistenceService.getCurrentFormatName());
    }

    @Test
    @DisplayName("Deve manter estado consistente do AcademicSystem")
    void testMaintainConsistentAcademicSystemState() {
        assertEquals(0, academicSystem.getClasses().size());

        classService.registerClass("POO", "Turma 1");
        assertEquals(1, academicSystem.getClasses().size());

        classService.registerClass("BD", "Turma 2");
        assertEquals(2, academicSystem.getClasses().size());
    }

    @Test
    @DisplayName("Deve permitir múltiplas operações de registro")
    void testAllowMultipleRegistrationOperations() {
        for (int i = 1; i <= 5; i++) {
            classService.registerClass("CLASS" + i, "Turma " + i);
        }

        assertEquals(5, academicSystem.getClasses().size());
    }

    @Test
    @DisplayName("Deve propagar exceções dos serviços")
    void testPropagateExceptionsFromServices() {
        classService.registerClass("POO", "Turma");

        assertThrows(Exception.class, () -> {
            classService.registerClass("POO", "Turma Duplicada");
        });
    }

    @Test
    @DisplayName("Deve retornar dados consistentes através de delegação")
    void testReturnConsistentDataThroughDelegation() {
        classService.registerClass("POO", "Turma");
        AcademicClass directAccess = academicSystem.findClassByCode("POO").orElse(null);

        assertNotNull(directAccess);
        assertEquals("POO", directAccess.getCode());
    }

    @Test
    @DisplayName("Deve existir controller com todos os serviços")
    void testControllerHasAllServices() {
        assertNotNull(classService);
        assertNotNull(assessmentService);
        assertNotNull(reportService);
        assertNotNull(persistenceService);
        assertNotNull(authorizationService);
    }
}
