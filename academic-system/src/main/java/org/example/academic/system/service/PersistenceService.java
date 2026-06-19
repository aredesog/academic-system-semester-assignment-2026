package org.example.academic.system.service;

import org.example.academic.system.model.AcademicClass;
import org.example.academic.system.model.PersistenceType;
import org.example.academic.system.repository.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class PersistenceService {
    private static final Path CONFIG_FILE = Path.of("persistence_config.txt");

    // Guarda a estratégia de persistência configurada atualmente
    private PersistenceStrategy currentStrategy;
    private PersistenceType currentType;

    public PersistenceService() {
        configurePersistenceType(loadConfiguredPersistenceType(), false);
    }

    /**
     * Altera o tipo de persistência ativo no sistema (US-2372)
     * Esse metodo deve ser chamado pelo Controller após validar se o usuário é ADMIN
     */
    public void configurePersistenceType(PersistenceType type) {
        configurePersistenceType(type, true);
    }

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
                // Should never happen because all enum values are handled
                throw new IllegalArgumentException("Tipo de persistência inválido: " + type);
        }

        this.currentType = type;

        if (saveConfiguration) {
            saveConfiguredPersistenceType(type);
        }
    }

    /**
     * Delega o salvamento dos dados para o repositório configurado (US-2362, US-2373, US-2374)
     */
    public void save(List<AcademicClass> classes) {
        if (currentStrategy == null) {
            throw new IllegalStateException("Nenhuma estratégia de persistência configurada.");
        }
        // Executa o salvamento no formato atual sem alterar o modelo de domínio (AC6/AC7)
        currentStrategy.save(classes);
    }

    /**
     * CORRIGIDO / ADICIONADO:
     * Carrega as turmas gravadas no formato configurado atualmente (Estratégia Ativa)
     */
    public List<AcademicClass> load() {
        if (currentStrategy == null) {
            throw new IllegalStateException("Nenhuma estratégia de persistência configurada.");
        }
        // Delega a leitura para o repositório ativo no momento
        return currentStrategy.load();
    }

    /**
     * Retorna o nome do formato atual para o gerador de relatórios (US-2377)
     */
    public String getCurrentFormatName() {
        return currentStrategy != null ? currentStrategy.getFormatName() : "NENHUM";
    }

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

    private void saveConfiguredPersistenceType(PersistenceType type) {
        try {
            Files.writeString(CONFIG_FILE, type.name());
        } catch (IOException exception) {
            throw new IllegalStateException("Erro ao salvar a configuracao de persistencia.", exception);
        }
    }
}
