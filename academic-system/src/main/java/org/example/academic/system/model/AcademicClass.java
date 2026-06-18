package org.example.academic.system.model;

import org.example.academic.system.exception.AcademicSystemException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AcademicClass {
    private final String code;
    private final String title;
    private final List<Assessment> assessments;

    public AcademicClass(String code, String title) {
        if (isBlank(code)) {
            throw new AcademicSystemException("Class code cannot be empty.");
        }

        if (isBlank(title)) {
            throw new AcademicSystemException("Class title cannot be empty.");
        }

        this.code = code.trim();
        this.title = title.trim();
        this.assessments = new ArrayList<>();
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public void addAssessment(Assessment assessment) {
        if (assessment == null) {
            throw new AcademicSystemException("Assessment cannot be null.");
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

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
