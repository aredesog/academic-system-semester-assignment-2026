package org.example.academic.system.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.academic.system.model.AcademicClass;
import org.example.academic.system.model.Assessment;
import org.example.academic.system.model.Assignment;
import org.example.academic.system.model.Exam;
import org.example.academic.system.model.PracticalAssignment;
import org.example.academic.system.model.Seminar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JsonRepository implements PersistenceStrategy {

    @Override
    public void save(List<AcademicClass> classes) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            // Escreve os dados no arquivo JSON de forma identada/organizada
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("academic_data.json"), toStoredClasses(classes));
        } catch (Exception e) {
            System.err.println("Erro ao salvar os dados em JSON: " + e.getMessage());
        }
    }

    @Override
    public List<AcademicClass> load() {
        File file = new File("academic_data.json");

        // Se o arquivo ainda não existir (primeira execução), retorna a lista vazia
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            // Lê o arquivo JSON e deserializa para uma lista de AcademicClass
            List<StoredClass> storedClasses = mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, StoredClass.class));
            List<AcademicClass> classes = toAcademicClasses(storedClasses);
            System.out.println("Dados carregados do JSON com sucesso! Total de turmas: " + classes.size());
            return classes;
        } catch (Exception e) {
            System.err.println("Erro ao carregar os dados do JSON: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public String getFormatName() {
        return "JSON";
    }

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
