package org.example.academic.system.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class AcademicSystem {
    private final List<AcademicClass> classes;

    // Inicializa o sistema com uma lista vazia de turmas
    public AcademicSystem() {
        this.classes = new ArrayList<>();
    }

    // Adiciona uma nova turma à lista do sistema
    public void addClass(AcademicClass academicClass) {
        classes.add(academicClass);
    }

    // Substitui todas as turmas existentes pelas turmas carregadas (usada ao trocar tipo de persistência)
    public void replaceClasses(List<AcademicClass> loadedClasses) {
        classes.clear();

        if (loadedClasses != null) {
            classes.addAll(loadedClasses);
        }
    }

    // Busca uma turma pelo código, ignorando maiúsculas/minúsculas e espaços
    public Optional<AcademicClass> findClassByCode(String code) {
        if (code == null) {
            return Optional.empty();
        }

        return classes.stream()
                .filter(academicClass -> academicClass.getCode().equalsIgnoreCase(code.trim()))
                .findFirst();
    }

    // Retorna a lista de turmas como visão imutável para evitar modificações externas
    public List<AcademicClass> getClasses() {
        return Collections.unmodifiableList(classes);
    }
}
