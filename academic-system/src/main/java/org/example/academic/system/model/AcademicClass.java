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

    public AcademicClass(String code, String title) {
        this.code = code == null ? null : code.trim();
        this.title = title == null ? null : title.trim();
        this.assessments = new ArrayList<>();

        DomainValidator.validate(this);
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public void addAssessment(Assessment assessment) {
        if (assessment == null) {
            throw new AcademicSystemException("A avaliacao nao pode ser nula.");
        }

        assessments.add(assessment);
    }

    public List<Assessment> getAssessments() {
        return Collections.unmodifiableList(assessments);
    }

    @Override
    public String toString() {
        return code + " - " + title;
    }

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

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
