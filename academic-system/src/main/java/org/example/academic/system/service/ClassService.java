package org.example.academic.system.service;

import org.example.academic.system.exception.AcademicSystemException;
import org.example.academic.system.model.AcademicClass;
import org.example.academic.system.model.AcademicSystem;

public class ClassService {
    private final AcademicSystem academicSystem;

    public ClassService(AcademicSystem academicSystem) {
        this.academicSystem = academicSystem;
    }

    public void registerClass(String code, String title) {
        AcademicClass academicClass = new AcademicClass(code, title);

        if (academicSystem.findClassByCode(academicClass.getCode()).isPresent()) {
            throw new AcademicSystemException("A class with this code already exists.");
        }

        academicSystem.addClass(academicClass);
    }
}
