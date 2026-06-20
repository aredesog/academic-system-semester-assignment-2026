package org.example.academic.system.model;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da Classe Assessment e Subclasses")
class AssessmentTest {

    @Test
    @DisplayName("Deve criar Exam com dados válidos")
    void testCreateExamValid() {
        Exam exam = new Exam(8.5, 0.4);
        assertEquals(8.5, exam.getValue());
        assertEquals(0.4, exam.getWeight());
        assertEquals("Exam", exam.getType());
    }

    @Test
    @DisplayName("Deve criar Seminar com dados válidos")
    void testCreateSeminarValid() {
        Seminar seminar = new Seminar(7.0, 0.3);
        assertEquals(7.0, seminar.getValue());
        assertEquals(0.3, seminar.getWeight());
        assertEquals("Seminar", seminar.getType());
    }

    @Test
    @DisplayName("Deve criar Assignment com dados válidos")
    void testCreateAssignmentValid() {
        Assignment assignment = new Assignment(6.5, 0.3);
        assertEquals(6.5, assignment.getValue());
        assertEquals(0.3, assignment.getWeight());
        assertEquals("Assignment", assignment.getType());
    }

    @Test
    @DisplayName("Deve criar PracticalAssignment com dados válidos")
    void testCreatePracticalAssignmentValid() {
        PracticalAssignment practical = new PracticalAssignment(9.0, 0.2);
        assertEquals(9.0, practical.getValue());
        assertEquals(0.2, practical.getWeight());
        assertEquals("Practical Assignment", practical.getType());
    }

    @Test
    @DisplayName("Deve rejeitar value negativo")
    void testRejectNegativeValue() {
        assertThrows(Exception.class, () -> {
            new Exam(-1.0, 0.5);
        });
    }

    @Test
    @DisplayName("Deve aceitar value zero")
    void testAcceptZeroValue() {
        Exam exam = new Exam(0.0, 0.5);
        assertEquals(0.0, exam.getValue());
    }

    @Test
    @DisplayName("Deve rejeitar weight zero")
    void testRejectZeroWeight() {
        assertThrows(Exception.class, () -> {
            new Exam(5.0, 0.0);
        });
    }

    @Test
    @DisplayName("Deve rejeitar weight negativo")
    void testRejectNegativeWeight() {
        assertThrows(Exception.class, () -> {
            new Exam(5.0, -0.5);
        });
    }

    @Test
    @DisplayName("Deve aceitar weight fracionário")
    void testAcceptFractionalWeight() {
        Exam exam = new Exam(5.0, 0.333);
        assertEquals(0.333, exam.getWeight());
    }

    @Test
    @DisplayName("Deve retornar toString formatado")
    void testToStringFormatted() {
        Exam exam = new Exam(8.5, 0.4);
        String result = exam.toString();
        
        assertTrue(result.contains("Exam"));
        assertTrue(result.contains("8.5"));
        assertTrue(result.contains("0.4"));
    }

    @Test
    @DisplayName("Deve validar todas as constraints na construção")
    void testValidationOnConstruction() {
        assertThrows(Exception.class, () -> {
            new Exam(10.0, 0.0);
        });
    }

    @Test
    @DisplayName("Deve aceitar valores máximos")
    void testAcceptMaxValues() {
        Exam exam = new Exam(10.0, 1.0);
        assertEquals(10.0, exam.getValue());
        assertEquals(1.0, exam.getWeight());
    }

    @Test
    @DisplayName("Deve retornar tipo correto para cada subclasse")
    void testCorrectTypeForEachSubclass() {
        assertEquals("Exam", new Exam(5.0, 0.5).getType());
        assertEquals("Seminar", new Seminar(5.0, 0.5).getType());
        assertEquals("Assignment", new Assignment(5.0, 0.5).getType());
        assertEquals("Practical Assignment", new PracticalAssignment(5.0, 0.5).getType());
    }
}
