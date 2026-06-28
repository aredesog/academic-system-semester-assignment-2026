package org.example.academic.system.repository;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.example.academic.system.model.AcademicClass;
import org.example.academic.system.model.Assessment;
import org.example.academic.system.model.Assignment;
import org.example.academic.system.model.Exam;
import org.example.academic.system.model.PracticalAssignment;
import org.example.academic.system.model.Seminar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XmlRepository implements PersistenceStrategy {

    // Serializa a lista de turmas para o arquivo academic_data.xml usando Jackson XML
    @Override
    public void save(List<AcademicClass> classes) {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            // Escreve os dados no arquivo XML formatado de forma limpa
            xmlMapper.writerWithDefaultPrettyPrinter().writeValue(new File("academic_data.xml"), toStoredClasses(classes));
        } catch (Exception e) {
            System.err.println("Erro ao salvar os dados em XML: " + e.getMessage());
        }
    }

    // Lê o arquivo XML e reconstrói a lista de turmas com suas avaliações
    @Override
    public List<AcademicClass> load() {
        File file = new File("academic_data.xml");

        // Se o arquivo ainda não existir (primeira execução), retorna a lista vazia
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try {
            XmlMapper xmlMapper = new XmlMapper();
            // Lê o arquivo XML e deserializa para uma lista de AcademicClass
            List<StoredClass> storedClasses = xmlMapper.readValue(file, xmlMapper.getTypeFactory().constructCollectionType(List.class, StoredClass.class));
            List<AcademicClass> classes = toAcademicClasses(storedClasses);
            System.out.println("Dados carregados do XML com sucesso! Total de turmas: " + classes.size());
            return classes;
        } catch (Exception e) {
            System.err.println("Erro ao carregar os dados do XML: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    // Retorna o nome do formato para uso nos logs e relatórios
    @Override
    public String getFormatName() {
        return "XML";
    }

    // Converte a lista de AcademicClass para DTOs simples antes de serializar em XML
    private List<StoredClass> toStoredClasses(List<AcademicClass> classes) {
        List<StoredClass> storedClasses = new ArrayList<>();

        for (AcademicClass academicClass : classes) {
            StoredClass storedClass = new StoredClass();
            storedClass.code = academicClass.getCode();
            storedClass.title = academicClass.getTitle();

            for (Assessment assessment : academicClass.getAssessments()) {
                StoredAssessment storedAssessment = new StoredAssessment();
                storedAssessment.type = assessment.getType();
                storedAssessment.value = assessment.getValue();
                storedAssessment.weight = assessment.getWeight();
                storedClass.assessments.add(storedAssessment);
            }

            storedClasses.add(storedClass);
        }

        return storedClasses;
    }

    // Reconstrói objetos AcademicClass a partir dos DTOs lidos do arquivo XML
    private List<AcademicClass> toAcademicClasses(List<StoredClass> storedClasses) {
        List<AcademicClass> classes = new ArrayList<>();

        for (StoredClass storedClass : storedClasses) {
            AcademicClass academicClass = new AcademicClass(storedClass.code, storedClass.title);

            for (StoredAssessment storedAssessment : storedClass.assessments) {
                academicClass.addAssessment(createAssessment(storedAssessment));
            }

            classes.add(academicClass);
        }

        return classes;
    }

    // Instancia a subclasse correta de Assessment com base no tipo armazenado no XML
    private Assessment createAssessment(StoredAssessment storedAssessment) {
        String type = storedAssessment.type == null ? "" : storedAssessment.type.trim().toLowerCase();

        return switch (type) {
            case "exam" -> new Exam(storedAssessment.value, storedAssessment.weight);
            case "practical", "practical assignment" -> new PracticalAssignment(storedAssessment.value, storedAssessment.weight);
            case "seminar" -> new Seminar(storedAssessment.value, storedAssessment.weight);
            case "assignment" -> new Assignment(storedAssessment.value, storedAssessment.weight);
            default -> throw new IllegalArgumentException("Tipo de avaliacao invalido: " + storedAssessment.type);
        };
    }

    public static class StoredClass {
        public String code;
        public String title;
        public List<StoredAssessment> assessments = new ArrayList<>();
    }

    public static class StoredAssessment {
        public String type;
        public double value;
        public double weight;
    }
}
