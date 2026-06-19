package org.example.academic.system.service;

import org.example.academic.system.exception.AcademicSystemException;
import org.example.academic.system.model.AcademicClass;
import org.example.academic.system.model.AcademicSystem;
import org.example.academic.system.model.Assessment;
import org.example.academic.system.model.Assignment;
import org.example.academic.system.model.Exam;
import org.example.academic.system.model.PracticalAssignment;
import org.example.academic.system.model.Seminar;

public class AssessmentService {
    private final AcademicSystem academicSystem;

    public AssessmentService(AcademicSystem academicSystem) {
        this.academicSystem = academicSystem;
    }

    public void registerAssessment(String classCode, String assessmentType, double value, double weight) {
        AcademicClass academicClass = academicSystem.findClassByCode(classCode)
                .orElseThrow(() -> new AcademicSystemException("Class not found."));

        Assessment assessment = createAssessment(assessmentType, value, weight);
        academicClass.addAssessment(assessment);
    }

    private Assessment createAssessment(String assessmentType, double value, double weight) {
        if (assessmentType == null) {
            throw new AcademicSystemException("Assessment type cannot be empty.");
        }

        return switch (assessmentType.trim().toLowerCase()) {
            case "exam" -> new Exam(value, weight);
            case "practical", "practical assignment" -> new PracticalAssignment(value, weight);
            case "seminar" -> new Seminar(value, weight);
            case "assignment" -> new Assignment(value, weight);
            default -> throw new AcademicSystemException("Invalid assessment type.");
        };
    }
}
