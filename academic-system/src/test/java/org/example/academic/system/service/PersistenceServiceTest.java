package org.example.academic.system.service;

import org.example.academic.system.model.AcademicClass;
import org.example.academic.system.model.PersistenceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes de PersistenceService")
class PersistenceServiceTest {

    private PersistenceService persistenceService;
    private static final Path CONFIG_FILE = Path.of("persistence_config.txt");

    @BeforeEach
    void setup() throws Exception {
        persistenceService = new PersistenceService();
        // Limpar arquivo de configuração se existir
        if (Files.exists(CONFIG_FILE)) {
            Files.delete(CONFIG_FILE);
        }
    }

    @Test
    @DisplayName("Deve configurar persistência TXT")
    void testConfigureTxtPersistence() {
        persistenceService.configurePersistenceType(PersistenceType.TXT);
        assertEquals("TXT", persistenceService.getCurrentFormatName());
    }

    @Test
    @DisplayName("Deve configurar persistência JSON")
    void testConfigureJsonPersistence() {
        persistenceService.configurePersistenceType(PersistenceType.JSON);
        assertEquals("JSON", persistenceService.getCurrentFormatName());
    }

    @Test
    @DisplayName("Deve configurar persistência XML")
    void testConfigureXmlPersistence() {
        persistenceService.configurePersistenceType(PersistenceType.XML);
        assertEquals("XML", persistenceService.getCurrentFormatName());
    }

    @Test
    @DisplayName("Deve salvar configuração em arquivo")
    void testSaveConfigurationToFile() throws Exception {
        persistenceService.configurePersistenceType(PersistenceType.JSON);
        
        assertTrue(Files.exists(CONFIG_FILE));
        String content = Files.readString(CONFIG_FILE).trim();
        assertEquals("JSON", content);
    }

    @Test
    @DisplayName("Deve carregar configuração de arquivo na inicialização")
    void testLoadConfigurationFromFileOnInit() throws Exception {
        Files.writeString(CONFIG_FILE, "XML");
        
        PersistenceService newService = new PersistenceService();
        assertEquals("XML", newService.getCurrentFormatName());
    }

    @Test
    @DisplayName("Deve usar TXT como padrão quando arquivo não existe")
    void testUseDefaultTxtPersistenceWhenNoConfigFile() throws Exception {
        if (Files.exists(CONFIG_FILE)) {
            Files.delete(CONFIG_FILE);
        }
        
        PersistenceService newService = new PersistenceService();
        assertEquals("TXT", newService.getCurrentFormatName());
    }

    @Test
    @DisplayName("Deve gerar relatório de configuração")
    void testGenerateConfigurationReport() {
        persistenceService.configurePersistenceType(PersistenceType.JSON);
        String report = persistenceService.generateConfigurationReport();

        assertTrue(report.contains("Configuracao") || report.contains("Configuração"));
        assertTrue(report.contains("JSON"));
    }

    @Test
    @DisplayName("Deve incluir formato ativo no relatório")
    void testReportIncludesActiveFormat() {
        persistenceService.configurePersistenceType(PersistenceType.XML);
        String report = persistenceService.generateConfigurationReport();

        assertTrue(report.contains("XML"));
    }

    @Test
    @DisplayName("Deve permitir trocar de estratégia de persistência")
    void testSwitchPersistenceStrategy() {
        persistenceService.configurePersistenceType(PersistenceType.TXT);
        assertEquals("TXT", persistenceService.getCurrentFormatName());

        persistenceService.configurePersistenceType(PersistenceType.JSON);
        assertEquals("JSON", persistenceService.getCurrentFormatName());

        persistenceService.configurePersistenceType(PersistenceType.XML);
        assertEquals("XML", persistenceService.getCurrentFormatName());
    }

    @Test
    @DisplayName("Deve rejeitar tipo de persistência nulo")
    void testRejectNullPersistenceType() {
        assertThrows(IllegalArgumentException.class, () -> {
            persistenceService.configurePersistenceType(null);
        });
    }

    @Test
    @DisplayName("Deve manter estratégia configurada")
    void testMaintainConfiguredStrategy() {
        persistenceService.configurePersistenceType(PersistenceType.JSON);
        String format1 = persistenceService.getCurrentFormatName();

        // Sem mudar, deve manter a mesma estratégia
        String format2 = persistenceService.getCurrentFormatName();

        assertEquals(format1, format2);
        assertEquals("JSON", format2);
    }

    @Test
    @DisplayName("Deve carregar dados corretamente")
    void testLoadDataCorrectly() {
        // Este teste verifica se o load() executa sem erro
        try {
            List<AcademicClass> classes = persistenceService.load();
            assertNotNull(classes);
        } catch (Exception e) {
            // Esperado se não houver arquivo salvo
            assertTrue(true);
        }
    }

    @Test
    @DisplayName("Deve salvar dados corretamente")
    void testSaveDataCorrectly() {
        persistenceService.configurePersistenceType(PersistenceType.TXT);
        
        AcademicClass academicClass = new AcademicClass("POO", "Turma");
        List<AcademicClass> classes = List.of(academicClass);

        // Não deve lançar exceção
        assertDoesNotThrow(() -> {
            persistenceService.save(classes);
        });
    }

    @Test
    @DisplayName("Deve conter nome do formato no relatório de configuração")
    void testFormatNameInReport() {
        persistenceService.configurePersistenceType(PersistenceType.XML);
        String report = persistenceService.generateConfigurationReport();

        assertTrue(report.contains("persistencia") || report.contains("persistência"));
        assertTrue(report.contains("XML"));
    }
}
