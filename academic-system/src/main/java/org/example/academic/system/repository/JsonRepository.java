package org.example.academic.system.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.academic.system.model.AcademicClass;

import java.io.File;
import java.util.ArrayList; // ADICIONADO
import java.util.List;      // ADICIONADO

public class JsonRepository implements PersistenceStrategy {

    @Override
    public void save(List<AcademicClass> classes) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            // Escreve os dados no arquivo JSON de forma identada/organizada
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("academic_data.json"), classes);
        } catch (Exception e) {
            System.err.println("Erro ao salvar os dados em JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<AcademicClass> load() {
        return new ArrayList<>();
    }

    @Override
    public String getFormatName() {
        // ADICIONADO: Método que a interface exige para identificar o formato
        return "JSON";
    }
}