package org.example.academic.system.repository;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.example.academic.system.model.AcademicClass;

import java.io.File;
import java.util.ArrayList; // ADICIONADO
import java.util.List;      // ADICIONADO

public class XmlRepository implements PersistenceStrategy {

    @Override
    public void save(List<AcademicClass> classes) {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            // Escreve os dados no arquivo XML formatado de forma limpa
            xmlMapper.writerWithDefaultPrettyPrinter().writeValue(new File("academic_data.xml"), classes);
        } catch (Exception e) {
            System.err.println("Erro ao salvar os dados em XML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<AcademicClass> load() {
        // CORRIGIDO: Retorna uma lista vazia mutável para não dar erro de compilação
        return new ArrayList<>();
    }

    @Override
    public String getFormatName() {
        return "XML";
    }
}