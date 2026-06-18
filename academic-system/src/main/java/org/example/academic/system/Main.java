package org.example.academic.system;

import org.example.academic.system.controller.AcademicSystemController;
import org.example.academic.system.model.AcademicSystem;
import org.example.academic.system.service.AssessmentService;
import org.example.academic.system.service.ClassService;
import org.example.academic.system.view.ConsoleMenu;

public class Main {
    public static void main(String[] args) {
        AcademicSystem academicSystem = new AcademicSystem();
        ClassService classService = new ClassService(academicSystem);
        AssessmentService assessmentService = new AssessmentService(academicSystem);
        AcademicSystemController controller = new AcademicSystemController(
                classService,
                assessmentService,
                academicSystem
        );
        ConsoleMenu menu = new ConsoleMenu(controller);

        menu.start();
    }
}
