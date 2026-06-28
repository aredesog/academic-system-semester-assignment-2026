package org.example.academic.system.service;

import org.example.academic.system.model.AcademicClass;
import org.example.academic.system.model.PersistenceType;
import org.example.academic.system.report.ReportGenerator;
import org.example.academic.system.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class PersistenceService {

    private static final Logger logger = LoggerFactory.getLogger(PersistenceService.class);

    private static final Path CONFIG_FILE = Path.of("persistence_config.txt");

    private PersistenceStrategy currentStrategy;

    // Inicializa o serviço carregando o tipo de persistência salvo anteriormente
    public PersistenceService() {
        configurePersistenceType(loadConfiguredPersistenceType(), false);
    }

    // Ponto de entrada público para trocar o tipo de persistência e salvar a nova configuração
    public void configurePersistenceType(PersistenceType type) {
        configurePersistenceType(type, true);
    }

    // Define a estratégia de persistência ativa e opcionalmente persiste a escolha no arquivo de configuração
    private void configurePersistenceType(PersistenceType type, boolean saveConfiguration) {
        if (type == null) {
            throw new IllegalArgumentException("Tipo de persistência não pode ser nulo.");
        }

        switch (type) {
            case TXT:
                this.currentStrategy = new TxtRepository();
                break;
            case JSON:
                this.currentStrategy = new JsonRepository();
                break;
            case XML:
                this.currentStrategy = new XmlRepository();
                break;
            default:
                throw new IllegalArgumentException("Tipo de persistência inválido: " + type);
        }

        logger.info("Persistence type configured to: {}", type);

        if (saveConfiguration) {
            saveConfiguredPersistenceType(type);
        }
    }

    // Delega o salvamento das turmas para a estratégia de persistência atualmente ativa
    public void save(List<AcademicClass> classes) {
        if (currentStrategy == null) {
            throw new IllegalStateException("Nenhuma estratégia de persistência configurada.");
        }
        logger.info("Saving {} class(es) using {} persistence", classes.size(), currentStrategy.getFormatName());
        currentStrategy.save(classes);
        logger.info("Data saved successfully using {} format", currentStrategy.getFormatName());
    }

    // Delega o carregamento das turmas para a estratégia de persistência atualmente ativa
    public List<AcademicClass> load() {
        if (currentStrategy == null) {
            throw new IllegalStateException("Nenhuma estratégia de persistência configurada.");
        }
        logger.info("Loading data using {} persistence", currentStrategy.getFormatName());
        List<AcademicClass> classes = currentStrategy.load();
        logger.info("Loaded {} class(es) from {} persistence", classes.size(), currentStrategy.getFormatName());
        return classes;
    }

    // Retorna o nome do formato de persistência atualmente configurado
    public String getCurrentFormatName() {
        return currentStrategy != null ? currentStrategy.getFormatName() : "NENHUM";
    }

    // Gera relatório textual indicando qual tipo de persistência está ativo
    public String generateConfigurationReport() {
        logger.info("Generating persistence configuration report. Active format: {}", getCurrentFormatName());
        return ReportGenerator.persistenceConfigurationReport(getCurrentFormatName());
    }

    // Lê o tipo de persistência salvo no arquivo de configuração; retorna TXT como padrão se não encontrado
    private PersistenceType loadConfiguredPersistenceType() {
        if (!Files.exists(CONFIG_FILE)) {
            return PersistenceType.TXT;
        }

        try {
            String configuredType = Files.readString(CONFIG_FILE).trim();

            if (configuredType.isEmpty()) {
                return PersistenceType.TXT;
            }

            return PersistenceType.valueOf(configuredType.toUpperCase());
        } catch (IllegalArgumentException | IOException exception) {
            return PersistenceType.TXT;
        }
    }

    // Grava o tipo de persistência escolhido no arquivo de configuração para recuperação futura
    private void saveConfiguredPersistenceType(PersistenceType type) {
        try {
            Files.writeString(CONFIG_FILE, type.name());
        } catch (IOException exception) {
            throw new IllegalStateException("Erro ao salvar a configuracao de persistencia.", exception);
        }
    }
}
