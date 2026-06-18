package org.example.academic.system.view;

import org.example.academic.system.controller.AcademicSystemController;
import org.example.academic.system.exception.AcademicSystemException;
import org.example.academic.system.model.AcademicClass;
import org.example.academic.system.model.Assessment;

import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {
    private final AcademicSystemController controller;
    private final Scanner scanner;

    public ConsoleMenu(AcademicSystemController controller) {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        int option;

        do {
            showMenu();
            option = readInteger("Escolha uma opcao: ");
            handleOption(option);
        } while (option != 4);
    }

    private void showMenu() {
        System.out.println();
        System.out.println("1 - Cadastrar turma");
        System.out.println("2 - Cadastrar avaliacao");
        System.out.println("3 - Listar turmas");
        System.out.println("4 - Sair");
    }

    private void handleOption(int option) {
        try {
            switch (option) {
                case 1 -> registerClass();
                case 2 -> registerAssessment();
                case 3 -> listClasses();
                case 4 -> System.out.println("Programa encerrado.");
                default -> System.out.println("Opcao invalida.");
            }
        } catch (AcademicSystemException exception) {
            System.out.println("Erro: " + exception.getMessage());
        }
    }

    private void registerClass() {
        String code = readText("Codigo da turma: ");
        String title = readText("Titulo da turma: ");

        controller.registerClass(code, title);
        System.out.println("Turma cadastrada com sucesso.");
    }

    private void registerAssessment() {
        String classCode = readText("Codigo da turma: ");
        String assessmentType = readAssessmentType();
        double value = readDouble("Valor da avaliacao: ");
        double weight = readDouble("Peso da avaliacao: ");

        controller.registerAssessment(classCode, assessmentType, value, weight);
        System.out.println("Avaliacao cadastrada com sucesso.");
    }

    private void listClasses() {
        List<AcademicClass> classes = controller.listClasses();

        if (classes.isEmpty()) {
            System.out.println("Nenhuma turma cadastrada.");
            return;
        }

        for (AcademicClass academicClass : classes) {
            System.out.println(academicClass);

            if (academicClass.getAssessments().isEmpty()) {
                System.out.println("  Nenhuma avaliacao cadastrada.");
                continue;
            }

            for (Assessment assessment : academicClass.getAssessments()) {
                System.out.println("  - " + assessment);
            }
        }
    }

    private String readText(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private String readAssessmentType() {
        System.out.println("Tipo da avaliacao:");
        System.out.println("1 - Exame");
        System.out.println("2 - Atividade pratica");
        System.out.println("3 - Seminario");
        System.out.println("4 - Trabalho");

        while (true) {
            int option = readInteger("Escolha o tipo da avaliacao: ");

            switch (option) {
                case 1 -> {
                    return "exam";
                }
                case 2 -> {
                    return "practical";
                }
                case 3 -> {
                    return "seminar";
                }
                case 4 -> {
                    return "assignment";
                }
                default -> System.out.println("Tipo de avaliacao invalido.");
            }
        }
    }

    private int readInteger(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException exception) {
                System.out.println("Numero invalido. Tente novamente.");
            }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String value = scanner.nextLine().trim().replace(',', '.');
                return Double.parseDouble(value);
            } catch (NumberFormatException exception) {
                System.out.println("Numero invalido. Tente novamente.");
            }
        }
    }
}
