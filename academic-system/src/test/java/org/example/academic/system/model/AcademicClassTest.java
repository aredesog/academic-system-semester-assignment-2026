package org.example.academic.system.model;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da Classe AcademicClass")
class AcademicClassTest {

    private AcademicClass academicClass;

    @BeforeEach
    void setup() {
        academicClass = new AcademicClass("POO", "Programação Orientada a Objetos");
    }

    @Test
    @DisplayName("Deve criar AcademicClass com dados válidos")
    void testCreateValidClass() {
        assertNotNull(academicClass);
        assertEquals("POO", academicClass.getCode());
        assertEquals("Programação Orientada a Objetos", academicClass.getTitle());
    }

    @Test
    @DisplayName("Deve rejeitar code nulo")
    void testRejectNullCode() {
        assertThrows(Exception.class, () -> {
            new AcademicClass(null, "Valido");
        });
    }

    @Test
    @DisplayName("Deve rejeitar code vazio")
    void testRejectBlankCode() {
        assertThrows(Exception.class, () -> {
            new AcademicClass("", "Valido");
        });
    }

    @Test
    @DisplayName("Deve rejeitar title nulo")
    void testRejectNullTitle() {
        assertThrows(Exception.class, () -> {
            new AcademicClass("POO", null);
        });
    }

    @Test
    @DisplayName("Deve rejeitar title vazio")
    void testRejectBlankTitle() {
        assertThrows(Exception.class, () -> {
            new AcademicClass("POO", "   ");
        });
    }

    @Test
    @DisplayName("Deve considerar iguais duas turmas com mesmo code")
    void testEqualityBasedOnCode() {
        AcademicClass other = new AcademicClass("POO", "Outro Título");
        assertEquals(academicClass, other);
    }

    @Test
    @DisplayName("Deve considerar diferentes duas turmas com code diferentes")
    void testInequalityDifferentCode() {
        AcademicClass other = new AcademicClass("BD", "Banco de Dados");
        assertNotEquals(academicClass, other);
    }

    @Test
    @DisplayName("Deve ter mesmo hashCode para turmas iguais")
    void testHashCodeEqualityForEqualObjects() {
        AcademicClass other = new AcademicClass("POO", "Outro Título");
        assertEquals(academicClass.hashCode(), other.hashCode());
    }

    @Test
    @DisplayName("Deve adicionar avaliação na turma")
    void testAddAssessment() {
        Assessment exam = new Exam(7.5, 0.5);
        academicClass.addAssessment(exam);
        assertEquals(1, academicClass.getAssessments().size());
        assertTrue(academicClass.getAssessments().contains(exam));
    }

    @Test
    @DisplayName("Deve rejeitar avaliação nula")
    void testRejectNullAssessment() {
        assertThrows(Exception.class, () -> {
            academicClass.addAssessment(null);
        });
    }

    @Test
    @DisplayName("Deve retornar lista imutável de avaliações")
    void testGetAssessmentsReturnsImmutableList() {
        academicClass.addAssessment(new Exam(7.5, 0.5));
        var assessments = academicClass.getAssessments();
        
        assertThrows(UnsupportedOperationException.class, () -> {
            assessments.add(new Exam(5.0, 0.5));
        });
    }

    @Test
    @DisplayName("Deve trim code e title com espaços")
    void testTrimCodeAndTitle() {
        AcademicClass trimmed = new AcademicClass("  POO  ", "  Programação  ");
        assertEquals("POO", trimmed.getCode());
        assertEquals("Programação", trimmed.getTitle());
    }

    @Test
    @DisplayName("Deve ser igual a si mesmo")
    void testEqualityWithSelf() {
        assertEquals(academicClass, academicClass);
    }

    @Test
    @DisplayName("Não deve ser igual a object de outro tipo")
    void testNotEqualToOtherType() {
        assertNotEquals(academicClass, "POO");
        assertNotEquals(academicClass, 123);
        assertNotEquals(academicClass, null);
    }
}
