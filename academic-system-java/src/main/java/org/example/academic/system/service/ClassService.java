package org.example.academic.system.service;

import org.example.academic.system.exception.AcademicSystemException;
import org.example.academic.system.model.AcademicClass;
import org.example.academic.system.model.AcademicSystem;

public class ClassService {
    private final AcademicSystem academicSystem;

    // Injeta o modelo central do sistema para acesso às turmas
    public ClassService(AcademicSystem academicSystem) {
        this.academicSystem = academicSystem;
    }

    // Cria e registra uma nova turma, garantindo que o código ainda não exista no sistema
    public void registerClass(String code, String title) {
        AcademicClass academicClass = new AcademicClass(code, title);

        if (academicSystem.findClassByCode(academicClass.getCode()).isPresent()) {
            throw new AcademicSystemException("Ja existe uma turma com este codigo.");
        }

        academicSystem.addClass(academicClass);
    }
}
