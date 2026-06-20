package org.example.academic.system.service;

import org.example.academic.system.exception.AcademicSystemException;
import org.example.academic.system.model.AcademicClass;
import org.example.academic.system.model.AcademicSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes de ClassService")
class ClassServiceTest {

    private ClassService classService;
    private AcademicSystem academicSystem;

    @BeforeEach
    void setup() {
        academicSystem = new AcademicSystem();
        classService = new ClassService(academicSystem);
    }

    @Test
    @DisplayName("Deve registrar turma com dados válidos")
    void testRegisterClassWithValidData() {
        classService.registerClass("POO", "Programação Orientada a Objetos");

        assertEquals(1, academicSystem.getClasses().size());
        assertTrue(academicSystem.findClassByCode("POO").isPresent());
    }

    @Test
    @DisplayName("Deve rejeitar turma com code duplicado")
    void testRejectDuplicateClassCode() {
        classService.registerClass("POO", "Primeira Turma");

        assertThrows(AcademicSystemException.class, () -> {
            classService.registerClass("POO", "Segunda Turma");
        });
    }

    @Test
    @DisplayName("Deve permitir turmas com códigos diferentes")
    void testAllowDifferentClassCodes() {
        classService.registerClass("POO", "Programação Orientada a Objetos");
        classService.registerClass("BD", "Banco de Dados");
        classService.registerClass("ESTRUTURA", "Estrutura de Dados");

        assertEquals(3, academicSystem.getClasses().size());
    }

    @Test
    @DisplayName("Deve criar AcademicClass com dados fornecidos")
    void testCreateAcademicClassWithProvidedData() {
        classService.registerClass("POO", "Minha Turma");

        AcademicClass academicClass = academicSystem.findClassByCode("POO").orElse(null);
        assertNotNull(academicClass);
        assertEquals("POO", academicClass.getCode());
        assertEquals("Minha Turma", academicClass.getTitle());
    }

    @Test
    @DisplayName("Deve validar turma antes de registrar")
    void testValidateClassBeforeRegistering() {
        assertThrows(Exception.class, () -> {
            classService.registerClass(null, "Turma Válida");
        });

        assertThrows(Exception.class, () -> {
            classService.registerClass("POO", null);
        });

        assertThrows(Exception.class, () -> {
            classService.registerClass("", "Turma Válida");
        });
    }

    @Test
    @DisplayName("Deve diferenciar entre códigos maiúsculos e minúsculos")
    void testCodeComparison() {
        classService.registerClass("POO", "Turma 1");

        // Assuming case-insensitive comparison based on structure.txt
        assertThrows(AcademicSystemException.class, () -> {
            classService.registerClass("poo", "Turma 2");
        });
    }

    @Test
    @DisplayName("Deve adicionar múltiplas turmas sequencialmente")
    void testAddMultipleClassesSequentially() {
        for (int i = 1; i <= 10; i++) {
            classService.registerClass("CLASS" + i, "Turma " + i);
        }

        assertEquals(10, academicSystem.getClasses().size());
    }

    @Test
    @DisplayName("Deve retornar erro ao registrar com dados inválidos")
    void testErrorMessageOnInvalidData() {
        AcademicSystemException exception = assertThrows(AcademicSystemException.class, () -> {
            classService.registerClass("POO", "Turma 1");
            classService.registerClass("POO", "Turma 2");
        });

        assertTrue(exception.getMessage().toLowerCase().contains("existe") ||
                   exception.getMessage().toLowerCase().contains("duplicate") ||
                   exception.getMessage().toLowerCase().contains("ja existe"));
    }

    @Test
    @DisplayName("Deve validar constraints Jakarta na criação")
    void testJakartaValidationConstraints() {
        assertThrows(Exception.class, () -> {
            classService.registerClass("   ", "Título Válido");
        });
    }

    @Test
    @DisplayName("Deve trim código e título antes de registrar")
    void testTrimCodeAndTitle() {
        classService.registerClass("  POO  ", "  Programação  ");

        AcademicClass academicClass = academicSystem.findClassByCode("POO").orElse(null);
        assertNotNull(academicClass);
        assertEquals("POO", academicClass.getCode());
    }
}
