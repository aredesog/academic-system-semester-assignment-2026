package org.example.academic.system;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.example.academic.system.controller.AcademicSystemController;
import org.example.academic.system.controller.AuthenticationController;
import org.example.academic.system.model.AcademicClass;
import org.example.academic.system.model.Assessment;
import org.example.academic.system.model.PersistenceType;
import org.example.academic.system.model.Role;
import org.example.academic.system.model.User;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.List;

public class MainGUI extends Application {

    private AuthenticationController authController;
    private AcademicSystemController academicController;
    private Stage primaryStage;
    private User loggedInUser;

    private static AuthenticationController savedAuthController;
    private static AcademicSystemController savedAcademicController;
    private static MainGUI instance;

    /**
     * Define os controladores da aplicação vindos do fluxo externo (Launcher/Main)
     */
    public void setControllers(AuthenticationController authController, AcademicSystemController academicController) {
        this.authController = authController;
        this.academicController = academicController;
        savedAuthController = authController;
        savedAcademicController = academicController;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Sistema de Gestão Acadêmica");

        // Recupera os controladores injetados via Main estático ou inicializa instâncias fallback robustas
        if (savedAuthController != null) {
            this.authController = savedAuthController;
        } else if (this.authController == null) {
            this.authController = new AuthenticationController(
                    new org.example.academic.system.security.AuthenticationService(
                            new org.example.academic.system.repository.TxtUserRepository()
                    )
            );
        }

        if (savedAcademicController != null) {
            this.academicController = savedAcademicController;
        } else if (this.academicController == null) {
            var academicSystem = new org.example.academic.system.model.AcademicSystem();
            this.academicController = new AcademicSystemController(
                    new org.example.academic.system.service.ClassService(academicSystem),
                    new org.example.academic.system.service.AssessmentService(academicSystem),
                    new org.example.academic.system.service.ReportService(academicSystem),
                    academicSystem,
                    new org.example.academic.system.service.PersistenceService(),
                    new org.example.academic.system.security.AuthorizationService()
            );
        }

        showLoginScreen();
    }

    /**
     * TUS-2407: Criação da tela gráfica de Login com validação e feedback
     */
    private void showLoginScreen() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #f4f6f9;");

        Label titleLabel = new Label("Acesso ao Sistema");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        Label userLabel = new Label("Usuário:");
        TextField userField = new TextField();
        userField.setPromptText("Digite seu usuário");

        Label passLabel = new Label("Senha:");
        PasswordField passField = new PasswordField();
        passField.setPromptText("Digite sua senha");

        grid.add(userLabel, 0, 0);
        grid.add(userField, 1, 0);
        grid.add(passLabel, 0, 1);
        grid.add(passField, 1, 1);

        Button loginButton = new Button("Entrar");
        loginButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 20 8 20; -fx-cursor: hand;");

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");

        loginButton.setOnAction(e -> {
            String username = userField.getText().trim();
            String password = passField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Por favor, preencha todos os campos.");
                return;
            }

            try {
                User user = authController.login(username, password);
                if (user != null) {
                    this.loggedInUser = user;
                    showMainDashboard();
                } else {
                    errorLabel.setText("Credenciais inválidas.");
                }
            } catch (Exception ex) {
                errorLabel.setText("Erro de autenticação: " + ex.getMessage());
            }
        });

        root.getChildren().addAll(titleLabel, grid, loginButton, errorLabel);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    /**
     * TUS-2408: Painel principal com menu dinâmico baseado na Role do usuário autenticado
     */
    private void showMainDashboard() {
        BorderPane mainLayout = new BorderPane();

        // Barra Superior informativa
        HBox topBar = new HBox(15);
        topBar.setPadding(new Insets(15));
        topBar.setStyle("-fx-background-color: #2c3e50;");
        topBar.setAlignment(Pos.CENTER_LEFT);

        Label welcomeLabel = new Label("Usuário: " + loggedInUser.getUsername() + " | Nível de Acesso: " + loggedInUser.getRole());
        welcomeLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button logoutButton = new Button("Sair (Logout)");
        logoutButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
        logoutButton.setOnAction(e -> {
            try {
                authController.logout();
            } catch (Exception ex) {
                // Logout local preventivo
            }
            loggedInUser = null;
            showLoginScreen();
        });

        topBar.getChildren().addAll(welcomeLabel, spacer, logoutButton);
        mainLayout.setTop(topBar);

        // Sidebar (Menu de Navegação Lateral)
        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(15));
        sidebar.setStyle("-fx-background-color: #34495e;");
        sidebar.setPrefWidth(220);

        Button btnVisualization = createSidebarButton("Visualizar Dados");
        Button btnAddClass = createSidebarButton("Cadastrar Turma");
        Button btnAddAssessment = createSidebarButton("Cadastrar Avaliação");
        Button btnPersistence = createSidebarButton("Configurações de Banco");
        Button btnReports = createSidebarButton("Central de Relatórios");

        // Montagem do menu com base nas permissões RBAC
        if (loggedInUser.getRole() == Role.ADMIN) {
            sidebar.getChildren().addAll(btnVisualization, btnAddClass, btnAddAssessment, btnPersistence, btnReports);
        } else if (loggedInUser.getRole() == Role.PROFESSOR) {
            sidebar.getChildren().addAll(btnVisualization, btnAddAssessment, btnReports);
        }

        mainLayout.setLeft(sidebar);

        // Define a visualização de listagem como tela padrão inicial
        mainLayout.setCenter(createVisualizationView());

        // Eventos de mapeamento dos botões de navegação
        btnVisualization.setOnAction(e -> mainLayout.setCenter(createVisualizationView()));
        btnAddClass.setOnAction(e -> mainLayout.setCenter(createAddClassView()));
        btnAddAssessment.setOnAction(e -> mainLayout.setCenter(createAddAssessmentView()));
        btnPersistence.setOnAction(e -> mainLayout.setCenter(createPersistenceView()));
        btnReports.setOnAction(e -> mainLayout.setCenter(createReportsView()));

        Scene scene = new Scene(mainLayout, 1000, 650);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
    }

    private Button createSidebarButton(String text) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #ecf0f1; -fx-font-size: 13px; -fx-padding: 10 15 10 15;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #4e6a85; -fx-text-fill: white; -fx-font-size: 13px; -fx-padding: 10 15 10 15;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #ecf0f1; -fx-font-size: 13px; -fx-padding: 10 15 10 15;"));
        return btn;
    }

    /**
     * TUS-2413: Tela de Visualização Unificada de Turmas e suas respectivas Avaliações
     */
    private Pane createVisualizationView() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        Label headLabel = new Label("Quadro Acadêmico - Turmas e Componentes de Avaliação");
        headLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        SplitPane splitPane = new SplitPane();
        splitPane.setDividerPositions(0.35);
        VBox.setVgrow(splitPane, Priority.ALWAYS);

        // Painel Esquerdo: Lista de Turmas
        VBox leftBox = new VBox(10);
        leftBox.setPadding(new Insets(5));
        Label classLabel = new Label("Selecione uma Turma:");
        classLabel.setStyle("-fx-font-weight: bold;");

        ListView<AcademicClass> classListView = new ListView<>();
        classListView.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(classListView, Priority.ALWAYS);
        leftBox.getChildren().addAll(classLabel, classListView);

        // Painel Direito: Tabela Dinâmica de Avaliações
        VBox rightBox = new VBox(10);
        rightBox.setPadding(new Insets(5));
        Label assessmentLabel = new Label("Avaliações Vinculadas à Turma:");
        assessmentLabel.setStyle("-fx-font-weight: bold;");

        TableView<Assessment> assessmentTable = new TableView<>();
        assessmentTable.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(assessmentTable, Priority.ALWAYS);

        TableColumn<Assessment, String> typeCol = new TableColumn<>("Tipo de Avaliação");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeCol.setPrefWidth(180);

        TableColumn<Assessment, Double> valueCol = new TableColumn<>("Nota / Valor Obtido");
        valueCol.setCellValueFactory(new PropertyValueFactory<>("value"));
        valueCol.setPrefWidth(130);

        TableColumn<Assessment, Double> weightCol = new TableColumn<>("Peso da Avaliação");
        weightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));
        weightCol.setPrefWidth(130);

        assessmentTable.getColumns().addAll(typeCol, valueCol, weightCol);
        rightBox.getChildren().addAll(assessmentLabel, assessmentTable);

        splitPane.getItems().addAll(leftBox, rightBox);

        // Carga inicial das turmas cadastradas no sistema
        try {
            List<AcademicClass> classes = academicController.listClasses();
            classListView.getItems().setAll(classes);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro de Carregamento", "Incapaz de ler turmas: " + e.getMessage());
        }

        // Listener reativo: ao selecionar uma turma, renderiza as avaliações dela no painel direito
        classListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                assessmentTable.getItems().setAll(newSelection.getAssessments());
            } else {
                assessmentTable.getItems().clear();
            }
        });

        root.getChildren().addAll(headLabel, splitPane);
        return root;
    }

    /**
     * TUS-2409: Form de Cadastro de Turma (Apenas para ADMIN)
     */
    private Pane createAddClassView() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setMaxWidth(550);

        Label headLabel = new Label("Registrar Nova Turma");
        headLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        Label codeLabel = new Label("Código Oficial da Turma:");
        TextField codeField = new TextField();
        codeField.setPromptText("Ex: COM222");

        Label titleLabel = new Label("Título da Disciplina:");
        TextField titleField = new TextField();
        titleField.setPromptText("Ex: Engenharia de Software");

        grid.add(codeLabel, 0, 0);
        grid.add(codeField, 1, 0);
        grid.add(titleLabel, 0, 1);
        grid.add(titleField, 1, 1);

        Button saveButton = new Button("Salvar e Persistir Turma");
        saveButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15 8 15;");

        saveButton.setOnAction(e -> {
            String code = codeField.getText().trim();
            String title = titleField.getText().trim();

            if (code.isEmpty() || title.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Campos Incompletos", "Preencha o código e o título.");
                return;
            }

            try {
                academicController.registerClass(code, title);
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Turma gravada e salva com sucesso!");
                codeField.clear();
                titleField.clear();
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Erro no Cadastro", ex.getMessage());
            }
        });

        root.getChildren().addAll(headLabel, grid, saveButton);
        return root;
    }

    /**
     * US-2410: Form de Cadastro de Avaliação com cálculo de tipos, notas e pesos
     */
    private Pane createAddAssessmentView() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setMaxWidth(550);

        Label headLabel = new Label("Adicionar Nova Avaliação");
        headLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        Label classLabel = new Label("Vincular à Turma:");
        ComboBox<AcademicClass> classComboBox = new ComboBox<>();
        classComboBox.setPromptText("Selecione a turma destino");

        try {
            classComboBox.getItems().setAll(academicController.listClasses());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao obter turmas disponíveis.");
        }

        Label typeLabel = new Label("Componente / Tipo:");
        ComboBox<String> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll("Exam", "Practical Assignment", "Seminar", "Assignment");
        typeComboBox.setValue("Exam");

        Label valueLabel = new Label("Nota Valor (Obtida):");
        TextField valueField = new TextField();
        valueField.setPromptText("Ex: 7.5");

        Label weightLabel = new Label("Peso Relativo (0 a 1.0):");
        TextField weightField = new TextField();
        weightField.setPromptText("Ex: 0.4");

        grid.add(classLabel, 0, 0);
        grid.add(classComboBox, 1, 0);
        grid.add(typeLabel, 0, 1);
        grid.add(typeComboBox, 1, 1);
        grid.add(valueLabel, 0, 2);
        grid.add(valueField, 1, 2);
        grid.add(weightLabel, 0, 3);
        grid.add(weightField, 1, 3);

        Button saveButton = new Button("Registrar Avaliação");
        saveButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15 8 15;");

        saveButton.setOnAction(e -> {
            AcademicClass selectedClass = classComboBox.getValue();
            String type = typeComboBox.getValue();
            String valRaw = valueField.getText().trim().replace(',', '.');
            String wRaw = weightField.getText().trim().replace(',', '.');

            if (selectedClass == null || valRaw.isEmpty() || wRaw.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Validação", "Todos os campos devem ser fornecidos.");
                return;
            }

            try {
                double value = Double.parseDouble(valRaw);
                double weight = Double.parseDouble(wRaw);

                academicController.registerAssessment(selectedClass.getCode(), type, value, weight);
                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Avaliação adicionada ao diário de classe!");
                valueField.clear();
                weightField.clear();
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Erro Numérico", "Nota e Peso de entrada devem ser decimais.");
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Inconsistência Acadêmica", ex.getMessage());
            }
        });

        root.getChildren().addAll(headLabel, grid, saveButton);
        return root;
    }

    /**
     * TUS-2412: Tela de Gerenciamento Estratégico da Persistência Multi-formato (Apenas ADMIN)
     */
    private Pane createPersistenceView() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setMaxWidth(650);

        Label headLabel = new Label("Painel de Configuração de Estratégias de Armazenamento");
        headLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        Label currentTitle = new Label("Status do Formato de Armazenamento Ativo:");
        currentTitle.setStyle("-fx-font-weight: bold;");
        TextArea statusReportArea = new TextArea();
        statusReportArea.setEditable(false);
        statusReportArea.setPrefHeight(70);

        // Atualizador local em tempo real da estratégia ativa
        Runnable reloadFormatStatus = () -> {
            try {
                statusReportArea.setText(academicController.generatePersistenceConfigurationReport());
            } catch (Exception ex) {
                statusReportArea.setText("Nenhuma configuração ativa carregada no momento.");
            }
        };
        reloadFormatStatus.run();

        HBox changeBox = new HBox(12);
        changeBox.setAlignment(Pos.CENTER_LEFT);
        Label comboLabel = new Label("Mudar Formato Ativo:");
        ComboBox<PersistenceType> comboFormat = new ComboBox<>();
        comboFormat.getItems().addAll(PersistenceType.values());
        comboFormat.setValue(PersistenceType.TXT);

        Button btnApplyFormat = new Button("Migrar e Salvar Banco");
        btnApplyFormat.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
        btnApplyFormat.setOnAction(e -> {
            PersistenceType type = comboFormat.getValue();
            if (type != null) {
                try {
                    academicController.configurePersistence(type);
                    showAlert(Alert.AlertType.INFORMATION, "Migração Concluída", "Banco alterado para " + type + " e sincronizado!");
                    reloadFormatStatus.run();
                } catch (Exception ex) {
                    showAlert(Alert.AlertType.ERROR, "Erro de Migração", ex.getMessage());
                }
            }
        });
        changeBox.getChildren().addAll(comboLabel, comboFormat, btnApplyFormat);

        VBox syncBox = new VBox(10);
        Label forceLabel = new Label("Gravação Manual:");
        forceLabel.setStyle("-fx-font-weight: bold;");
        Button btnForceSave = new Button("Forçar Commit / Salvamento Manual Imediato");
        btnForceSave.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 15 10 15;");
        btnForceSave.setOnAction(e -> {
            try {
                academicController.saveAcademicData();
                showAlert(Alert.AlertType.INFORMATION, "Sincronizado", "Dados acadêmicos gravados com sucesso no formato ativo!");
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Erro ao Gravar", ex.getMessage());
            }
        });
        syncBox.getChildren().addAll(forceLabel, btnForceSave);

        root.getChildren().addAll(headLabel, currentTitle, statusReportArea, changeBox, new Separator(), syncBox);
        return root;
    }

    /**
     * TUS-2411: Central de visualização de Relatórios Acadêmicos e validação de pesos
     */
    private Pane createReportsView() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        Label headLabel = new Label("Central do Emissor de Relatórios Técnicos e Gerenciais");
        headLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        HBox actionButtons = new HBox(10);
        Button btnSummary = new Button("Avaliações por Turma");
        Button btnWeights = new Button("Validação Geral de Pesos (Status)");
        Button btnPersist = new Button("Relatório de Persistência");

        actionButtons.getChildren().addAll(btnSummary, btnWeights);
        if (loggedInUser.getRole() == Role.ADMIN) {
            actionButtons.getChildren().add(btnPersist);
        }

        TextArea displayReport = new TextArea();
        displayReport.setEditable(false);
        displayReport.setFont(Font.font("Courier New", 12));
        VBox.setVgrow(displayReport, Priority.ALWAYS);

        btnSummary.setOnAction(e -> {
            try {
                displayReport.setText(academicController.generateClassAssessmentSummaryReport());
            } catch (Exception ex) {
                displayReport.setText("Erro gerando relatório: " + ex.getMessage());
            }
        });

        btnWeights.setOnAction(e -> {
            try {
                displayReport.setText(academicController.generateAssessmentWeightReport());
            } catch (Exception ex) {
                displayReport.setText("Erro gerando relatório: " + ex.getMessage());
            }
        });

        btnPersist.setOnAction(e -> {
            try {
                displayReport.setText(academicController.generatePersistenceConfigurationReport());
            } catch (Exception ex) {
                displayReport.setText("Erro gerando relatório: " + ex.getMessage());
            }
        });

        root.getChildren().addAll(headLabel, actionButtons, displayReport);
        return root;
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Ponto de entrada JavaFX padrão em concordância com a arquitetura do projeto
     */
    public static void main(String[] args) {
        var academicSystem = new org.example.academic.system.model.AcademicSystem();
        var classService = new org.example.academic.system.service.ClassService(academicSystem);
        var assessmentService = new org.example.academic.system.service.AssessmentService(academicSystem);
        var reportService = new org.example.academic.system.service.ReportService(academicSystem);
        var persistenceService = new org.example.academic.system.service.PersistenceService();
        var authServiceLogic = new org.example.academic.system.security.AuthorizationService();
        var userRepository = new org.example.academic.system.repository.TxtUserRepository();
        var authService = new org.example.academic.system.security.AuthenticationService(userRepository);

        try {
            var loadedClasses = persistenceService.load();
            academicSystem.replaceClasses(loadedClasses);
            System.out.println("[JavaFX] Banco de dados carregado com sucesso!");
        } catch (Exception e) {
            System.out.println("[JavaFX] Inicializando nova base limpa/vazia.");
        }

        AcademicSystemController acadController = new AcademicSystemController(
                classService, assessmentService, reportService, academicSystem, persistenceService, authServiceLogic
        );

        AuthenticationController authenticationController = new AuthenticationController(authService);

        instance = new MainGUI();
        instance.setControllers(authenticationController, acadController);

        launch(args);
    }
}