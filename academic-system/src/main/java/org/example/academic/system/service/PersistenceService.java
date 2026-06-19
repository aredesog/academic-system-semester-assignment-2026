package org.example.academic.system.service;

import org.example.academic.system.model.AcademicClass;
import org.example.academic.system.repository.*;
import java.util.List;

public class PersistenceService {

    // Guarda a estratégia de persistência configurada atualmente
    private PersistenceStrategy currentStrategy;

    public PersistenceService() {
        // Define o formato TXT como padrão inicial do sistema (ou o que preferir)
        this.currentStrategy = new TxtRepository();
    }

    /**
     * Altera o tipo de persistência ativo no sistema (US-2372)
     * Esse método deve ser chamado pelo Controller após validar se o usuário é ADMIN
     */
    public void configurePersistenceType(String type) {
        switch (type.toUpperCase()) {
            case "TXT":
                this.currentStrategy = new TxtRepository();
                break;
            case "JSON":
                this.currentStrategy = new JsonRepository();
                break;
            case "XML":
                this.currentStrategy = new XmlRepository();
                break;
            default:
                throw new IllegalArgumentException("Tipo de persistência inválido: " + type);
        }
        System.out.println("Configuração de persistência alterada para: " + type);
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
}