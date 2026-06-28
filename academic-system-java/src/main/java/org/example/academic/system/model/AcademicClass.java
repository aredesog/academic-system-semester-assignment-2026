package org.example.academic.system.model;

import jakarta.validation.constraints.NotBlank;
import org.example.academic.system.exception.AcademicSystemException;
import org.example.academic.system.validation.DomainValidator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class AcademicClass {
    @NotBlank(message = "O codigo da turma nao pode estar vazio.")
    private final String code;

    @NotBlank(message = "O titulo da turma nao pode estar vazio.")
    private final String title;

    private final List<Assessment> assessments;

    // Cria uma turma com código e título, removendo espaços extras e validando os campos
    public AcademicClass(String code, String title) {
        this.code = code == null ? null : code.trim();
        this.title = title == null ? null : title.trim();
        this.assessments = new ArrayList<>();

        DomainValidator.validate(this);
    }

    // Retorna o código da turma
    public String getCode() {
        return code;
    }

    // Retorna o título da turma
    public String getTitle() {
        return title;
    }

    // Adiciona uma avaliação à turma, rejeitando valores nulos
    public void addAssessment(Assessment assessment) {
        if (assessment == null) {
            throw new AcademicSystemException("A avaliacao nao pode ser nula.");
        }

        assessments.add(assessment);
    }

    // Retorna a lista de avaliações como visão imutável
    public List<Assessment> getAssessments() {
        return Collections.unmodifiableList(assessments);
    }

    // Representação textual da turma no formato "código - título"
    @Override
    public String toString() {
        return code + " - " + title;
    }

    // Duas turmas são iguais se tiverem o mesmo código
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof AcademicClass that)) {
            return false;
        }

        return Objects.equals(code, that.code);
    }

    // Hash baseado apenas no código da turma para consistência com equals
    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
