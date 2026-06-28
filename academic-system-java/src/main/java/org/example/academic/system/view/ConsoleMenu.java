package org.example.academic.system.view;

import org.example.academic.system.controller.AcademicSystemController;
import org.example.academic.system.controller.AuthenticationController;
import org.example.academic.system.exception.AcademicSystemException;
import org.example.academic.system.exception.AuthenticationException;
import org.example.academic.system.exception.AuthorizationException;
import org.example.academic.system.exception.KeyboardInputException;
import org.example.academic.system.model.AcademicClass;
import org.example.academic.system.model.Assessment;
import org.example.academic.system.model.PersistenceType;
import org.example.academic.system.model.Role;
import org.example.academic.system.model.User;

import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {
    private final AcademicSystemController controller;
    private final AuthenticationController authenticationController;
    private final Scanner scanner;

    // Injeta os controllers e cria o Scanner para leitura do console
    public ConsoleMenu(AcademicSystemController controller, AuthenticationController authenticationController) {
        this.controller = controller;
        this.authenticationController = authenticationController;
        this.scanner = new Scanner(System.in);
    }

    // Inicia o loop principal do sistema: autentica o usuário e exibe o menu correspondente ao papel dele
    public void start() {
        boolean running = true;

        while (running) {
            User user = login();

            if (user.getRole() == Role.ADMIN) {
                running = showAdminMenu();
            } else if (user.getRole() == Role.PROFESSOR) {
                running = showProfessorMenu();
            }
        }
    }

    // Solicita credenciais em loop até que o login seja bem-sucedido
    private User login() {
        while (true) {
            System.out.println();
            System.out.println("===== Sistema Academico =====");
            System.out.println();
            String username = readText("Usuario: ");
            String password = readText("Senha: ");

            try {
                User user = authenticationController.login(username, password);
                System.out.println("Login realizado com sucesso.");
                System.out.println("Bem-vindo, " + user.getUsername() + "!");
                return user;
            } catch (AuthenticationException exception) {
                System.out.println("Usuario ou senha invalidos.");
            }
        }
    }

    // Exibe o menu do administrador em loop, retornando false ao sair ou true ao fazer logout
    private boolean showAdminMenu() {
        int option;

        do {
            printAdminMenu();
            try {
                option = readInteger("Escolha uma opcao: ");

                switch (option) {
                    case 1 -> execute(this::registerClass);
                    case 2 -> execute(this::registerAssessment);
                    case 3 -> execute(this::listClasses);
                    case 4 -> execute(this::printClassAssessmentSummaryReport);
                    case 5 -> execute(this::printAssessmentWeightReport);
                    case 6 -> execute(this::configurePersistenceType);
                    case 7 -> execute(this::saveAcademicData);
                    case 8 -> execute(this::printPersistenceConfigurationReport);
                    case 9 -> {
                        authenticationController.logout();
                        System.out.println("Logout realizado com sucesso.");
                        return true;
                    }
                    case 0 -> {
                        System.out.println("Programa encerrado.");
                        return false;
                    }
                    default -> throw new KeyboardInputException("Opcao invalida.");
                }
            } catch (KeyboardInputException exception) {
                System.out.println("Erro de entrada: " + exception.getMessage());
            }
        } while (true);
    }

    // Exibe o menu do professor em loop com opções reduzidas, retornando false ao sair ou true ao fazer logout
    private boolean showProfessorMenu() {
        int option;

        do {
            printProfessorMenu();
            try {
                option = readInteger("Escolha uma opcao: ");

                switch (option) {
                    case 1 -> execute(this::registerAssessment);
                    case 2 -> execute(this::listClasses);
                    case 3 -> execute(this::printClassAssessmentSummaryReport);
                    case 4 -> execute(this::printAssessmentWeightReport);
                    case 5 -> {
                        authenticationController.logout();
                        System.out.println("Logout realizado com sucesso.");
                        return true;
                    }
                    case 0 -> {
                        System.out.println("Programa encerrado.");
                        return false;
                    }
                    default -> throw new KeyboardInputException("Opcao invalida.");
                }
            } catch (KeyboardInputException exception) {
                System.out.println("Erro de entrada: " + exception.getMessage());
            }
        } while (true);
    }

    // Imprime as opções disponíveis para o perfil ADMIN
    private void printAdminMenu() {
        System.out.println();
        System.out.println("===== Sistema Academico - ADMIN =====");
        System.out.println();
        System.out.println("1 - Cadastrar turma");
        System.out.println("2 - Cadastrar avaliacao");
        System.out.println("3 - Listar turmas");
        System.out.println("4 - Gerar relatorio de avaliacoes por turma");
        System.out.println("5 - Gerar relatorio de peso das avaliacoes");
        System.out.println("6 - Configurar tipo de persistencia");
        System.out.println("7 - Salvar dados academicos");
        System.out.println("8 - Gerar relatorio de configuracao de persistencia");
        System.out.println("9 - Logout");
        System.out.println("0 - Sair");
    }

    // Imprime as opções disponíveis para o perfil PROFESSOR
    private void printProfessorMenu() {
        System.out.println();
        System.out.println("===== Sistema Academico - PROFESSOR =====");
        System.out.println();
        System.out.println("1 - Cadastrar avaliacao");
        System.out.println("2 - Listar turmas");
        System.out.println("3 - Gerar relatorio de avaliacoes por turma");
        System.out.println("4 - Gerar relatorio de peso das avaliacoes");
        System.out.println("5 - Logout");
        System.out.println("0 - Sair");
    }

    // Executa uma ação do menu capturando e exibindo erros sem interromper o loop principal
    private void execute(MenuAction action) {
        try {
            action.run();
        } catch (AcademicSystemException exception) {
            System.out.println("Erro: " + exception.getMessage());
        } catch (AuthorizationException exception) {
            System.out.println("Acesso negado.");
        } catch (KeyboardInputException exception) {
            System.out.println("Erro de entrada: " + exception.getMessage());
        }
    }

    // Lê código e título do console e solicita o cadastro da turma ao controller
    private void registerClass() {
        String code = readText("Codigo da turma: ");
        String title = readText("Titulo da turma: ");

        controller.registerClass(code, title);
        System.out.println("Turma cadastrada com sucesso.");
    }

    // Lê os dados da avaliação do console e solicita o cadastro ao controller
    private void registerAssessment() {
        String classCode = readText("Codigo da turma: ");
        String assessmentType = readAssessmentType();
        double value = readDouble("Valor da avaliacao: ");
        double weight = readDouble("Peso da avaliacao: ");

        controller.registerAssessment(classCode, assessmentType, value, weight);
        System.out.println("Avaliacao cadastrada com sucesso.");
    }

    // Exibe no console todas as turmas com suas respectivas avaliações
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
                System.out.println("  - Tipo: " + formatAssessmentType(assessment)
                        + " | Valor: " + assessment.getValue()
                        + " | Peso: " + assessment.getWeight());
            }
        }
    }

    // Traduz o tipo de avaliação do inglês para o português para exibição no console
    private String formatAssessmentType(Assessment assessment) {
        return switch (assessment.getType()) {
            case "Exam" -> "Exame";
            case "Practical Assignment" -> "Atividade pratica";
            case "Seminar" -> "Seminario";
            case "Assignment" -> "Trabalho";
            default -> assessment.getType();
        };
    }

    // Solicita e exibe o relatório de avaliações por turma
    private void printClassAssessmentSummaryReport() {
        System.out.println(controller.generateClassAssessmentSummaryReport());
    }

    // Solicita e exibe o relatório de peso das avaliações por turma
    private void printAssessmentWeightReport() {
        System.out.println(controller.generateAssessmentWeightReport());
    }

    // Solicita o salvamento manual dos dados acadêmicos e confirma ao usuário
    private void saveAcademicData() {
        controller.saveAcademicData();
        System.out.println("Dados academicos salvos com sucesso.");
    }

    // Solicita e exibe o relatório de configuração de persistência ativa
    private void printPersistenceConfigurationReport() {
        System.out.println(controller.generatePersistenceConfigurationReport());
    }

    // Apresenta as opções de formato e aplica o tipo de persistência escolhido
    private void configurePersistenceType() {
        System.out.println("Tipo de persistencia:");
        System.out.println("1 - TXT");
        System.out.println("2 - JSON");
        System.out.println("3 - XML");

        while (true) {
            try {
                int option = readInteger("Escolha o tipo de persistencia: ");

                switch (option) {
                    case 1 -> {
                        controller.configurePersistence(PersistenceType.TXT);
                        System.out.println("Persistencia configurada como TXT.");
                        System.out.println("Turmas carregadas: " + controller.listClasses().size());
                        return;
                    }
                    case 2 -> {
                        controller.configurePersistence(PersistenceType.JSON);
                        System.out.println("Persistencia configurada como JSON.");
                        System.out.println("Turmas carregadas: " + controller.listClasses().size());
                        return;
                    }
                    case 3 -> {
                        controller.configurePersistence(PersistenceType.XML);
                        System.out.println("Persistencia configurada como XML.");
                        System.out.println("Turmas carregadas: " + controller.listClasses().size());
                        return;
                    }
                    default -> throw new KeyboardInputException("Tipo de persistencia invalido.");
                }
            } catch (KeyboardInputException exception) {
                System.out.println("Erro de entrada: " + exception.getMessage());
            }
        }
    }

    // Exibe um prompt e retorna o texto digitado pelo usuário
    private String readText(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    // Exibe um submenu para o usuário escolher o tipo de avaliação e retorna a string correspondente
    private String readAssessmentType() {
        System.out.println("Tipo da avaliacao:");
        System.out.println("1 - Exame");
        System.out.println("2 - Atividade pratica");
        System.out.println("3 - Seminario");
        System.out.println("4 - Trabalho");

        while (true) {
            try {
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
                    default -> throw new KeyboardInputException("Tipo de avaliacao invalido.");
                }
            } catch (KeyboardInputException exception) {
                System.out.println("Erro de entrada: " + exception.getMessage());
            }
        }
    }

    // Lê um número inteiro do console, lançando exceção se a entrada não for numérica
    private int readInteger(String prompt) {
        try {
            System.out.print(prompt);
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException exception) {
            throw new KeyboardInputException("Numero inteiro invalido.");
        }
    }

    // Lê um número decimal do console, aceitando vírgula como separador, lançando exceção se inválido
    private double readDouble(String prompt) {
        try {
            System.out.print(prompt);
            String value = scanner.nextLine().trim().replace(',', '.');
            return Double.parseDouble(value);
        } catch (NumberFormatException exception) {
            throw new KeyboardInputException("Numero decimal invalido.");
        }
    }

    private interface MenuAction {
        void run();
    }
}
