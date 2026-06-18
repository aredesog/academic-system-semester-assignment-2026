package org.example.academic.system.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class AcademicSystem {
    private final List<AcademicClass> classes;

    public AcademicSystem() {
        this.classes = new ArrayList<>();
    }

    public void addClass(AcademicClass academicClass) {
        classes.add(academicClass);
    }

    public Optional<AcademicClass> findClassByCode(String code) {
        if (code == null) {
            return Optional.empty();
        }

        return classes.stream()
                .filter(academicClass -> academicClass.getCode().equalsIgnoreCase(code.trim()))
                .findFirst();
    }

    public List<AcademicClass> getClasses() {
        return Collections.unmodifiableList(classes);
    }
}
