package org.example.academic.system.service;

import org.example.academic.system.exception.AcademicSystemException;
import org.example.academic.system.model.AcademicClass;
import org.example.academic.system.model.AcademicSystem;
import org.example.academic.system.model.Assessment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes de AssessmentService")
class AssessmentServiceTest {

    private AssessmentService assessmentService;
    private ClassService classService;
    private AcademicSystem academicSystem;

    @BeforeEach
    void setup() {
        academicSystem = new AcademicSystem();
        assessmentService = new AssessmentService(academicSystem);
        classService = new ClassService(academicSystem);
    }

    @Test
    @DisplayName("Deve registrar avaliação do tipo Exam")
    void testRegisterExamAssessment() {
        classService.registerClass("POO", "Turma");
        assessmentService.registerAssessment("POO", "exam", 8.5, 0.4);

        AcademicClass academicClass = academicSystem.findClassByCode("POO").orElse(null);
        assertNotNull(academicClass);
        assertEquals(1, academicClass.getAssessments().size());
    }

    @Test
    @DisplayName("Deve registrar avaliação do tipo Seminar")
    void testRegisterSeminarAssessment() {
        classService.registerClass("POO", "Turma");
        assessmentService.registerAssessment("POO", "seminar", 7.0, 0.3);

        AcademicClass academicClass = academicSystem.findClassByCode("POO").orElse(null);
        assertEquals(1, academicClass.getAssessments().size());
    }

    @Test
    @DisplayName("Deve registrar avaliação do tipo Assignment")
    void testRegisterAssignmentAssessment() {
        classService.registerClass("POO", "Turma");
        assessmentService.registerAssessment("POO", "assignment", 6.5, 0.3);

        AcademicClass academicClass = academicSystem.findClassByCode("POO").orElse(null);
        assertEquals(1, academicClass.getAssessments().size());
    }

    @Test
    @DisplayName("Deve registrar avaliação do tipo Practical Assignment")
    void testRegisterPracticalAssignmentAssessment() {
        classService.registerClass("POO", "Turma");
        assessmentService.registerAssessment("POO", "practical assignment", 9.0, 0.2);

        AcademicClass academicClass = academicSystem.findClassByCode("POO").orElse(null);
        assertEquals(1, academicClass.getAssessments().size());
    }

    @Test
    @DisplayName("Deve registrar avaliação do tipo Practical (alias)")
    void testRegisterPracticalAssessment() {
        classService.registerClass("POO", "Turma");
        assessmentService.registerAssessment("POO", "practical", 9.0, 0.2);

        AcademicClass academicClass = academicSystem.findClassByCode("POO").orElse(null);
        assertEquals(1, academicClass.getAssessments().size());
    }

    @Test
    @DisplayName("Deve rejeitar tipo de avaliação inválido")
    void testRejectInvalidAssessmentType() {
        classService.registerClass("POO", "Turma");

        assertThrows(AcademicSystemException.class, () -> {
            assessmentService.registerAssessment("POO", "invalid", 5.0, 0.5);
        });
    }

    @Test
    @DisplayName("Deve rejeitar avaliação para turma não existente")
    void testRejectAssessmentForNonExistentClass() {
        assertThrows(AcademicSystemException.class, () -> {
            assessmentService.registerAssessment("POO", "exam", 8.5, 0.4);
        });
    }

    @Test
    @DisplayName("Deve rejeitar tipo de avaliação nulo")
    void testRejectNullAssessmentType() {
        classService.registerClass("POO", "Turma");

        assertThrows(AcademicSystemException.class, () -> {
            assessmentService.registerAssessment("POO", null, 5.0, 0.5);
        });
    }

    @Test
    @DisplayName("Deve registrar múltiplas avaliações na mesma turma")
    void testRegisterMultipleAssessmentsInSameClass() {
        classService.registerClass("POO", "Turma");
        assessmentService.registerAssessment("POO", "exam", 8.5, 0.4);
        assessmentService.registerAssessment("POO", "seminar", 7.0, 0.3);
        assessmentService.registerAssessment("POO", "assignment", 6.5, 0.3);

        AcademicClass academicClass = academicSystem.findClassByCode("POO").orElse(null);
        assertEquals(3, academicClass.getAssessments().size());
    }

    @Test
    @DisplayName("Deve validar constraints de value e weight")
    void testValidateValueAndWeightConstraints() {
        classService.registerClass("POO", "Turma");

        assertThrows(Exception.class, () -> {
            assessmentService.registerAssessment("POO", "exam", -1.0, 0.5);
        });

        assertThrows(Exception.class, () -> {
            assessmentService.registerAssessment("POO", "exam", 5.0, 0.0);
        });

        assertThrows(Exception.class, () -> {
            assessmentService.registerAssessment("POO", "exam", 5.0, -0.5);
        });
    }

    @Test
    @DisplayName("Deve ser case-insensitive para tipo de avaliação")
    void testAssessmentTypeIsCaseInsensitive() {
        classService.registerClass("POO", "Turma");
        assessmentService.registerAssessment("POO", "EXAM", 8.5, 0.4);
        assessmentService.registerAssessment("POO", "ExAm", 7.5, 0.3);

        AcademicClass academicClass = academicSystem.findClassByCode("POO").orElse(null);
        assertEquals(2, academicClass.getAssessments().size());
    }

    @Test
    @DisplayName("Deve criar tipo correto de Assessment baseado na string")
    void testCreatesCorrectAssessmentType() {
        classService.registerClass("POO", "Turma");
        
        assessmentService.registerAssessment("POO", "exam", 8.5, 0.4);
        assessmentService.registerAssessment("POO", "seminar", 7.0, 0.3);

        AcademicClass academicClass = academicSystem.findClassByCode("POO").orElse(null);
        assertEquals(2, academicClass.getAssessments().size());
    }

    @Test
    @DisplayName("Deve registrar avaliações em múltiplas turmas")
    void testRegisterAssessmentsInMultipleClasses() {
        classService.registerClass("POO", "Turma 1");
        classService.registerClass("BD", "Turma 2");

        assessmentService.registerAssessment("POO", "exam", 8.5, 0.5);
        assessmentService.registerAssessment("BD", "seminar", 7.0, 0.5);

        assertEquals(1, academicSystem.findClassByCode("POO").get().getAssessments().size());
        assertEquals(1, academicSystem.findClassByCode("BD").get().getAssessments().size());
    }
}
