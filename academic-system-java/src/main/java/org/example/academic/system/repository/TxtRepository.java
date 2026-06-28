package org.example.academic.system.repository;

import org.example.academic.system.model.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TxtRepository implements PersistenceStrategy {

    @Override
    public void save(List<AcademicClass> classes) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("academic_data.txt"))) {
            for (AcademicClass academicClass : classes) {
                writer.write("Class Code: " + academicClass.getCode() + "\n");
                writer.write("Class Title: " + academicClass.getTitle() + "\n");
                writer.write("Assessments:\n");

                if (academicClass.getAssessments() != null) {
                    for (Assessment assessment : academicClass.getAssessments()) {
                        writer.write("  - Type: " + assessment.getType() +
                                " | Value: " + assessment.getValue() +
                                " | Weight: " + assessment.getWeight() + "\n");
                    }
                }
                writer.write("=========================================\n");
            }
        } catch (Exception e) {
            System.err.println("Erro ao salvar os dados em TXT: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<AcademicClass> load() {
        List<AcademicClass> classes = new ArrayList<>();
        File file = new File("academic_data.txt");

        // Se o arquivo ainda não existir (primeira execução), retorna a lista vazia
        if (!file.exists()) {
            return classes;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            AcademicClass currentClass = null;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("Class Code:")) {
                    String code = line.replace("Class Code:", "").trim();
                    String titleLine = reader.readLine();
                    String title = titleLine != null ? titleLine.replace("Class Title:", "").trim() : "";

                    // Recria a turma usando o construtor do Carlos Pereira
                    currentClass = new AcademicClass(code, title);
                    classes.add(currentClass);

                } else if (line.startsWith("- Type:") && currentClass != null) {
                    // Quebra a linha da avaliação para extrair os dados
                    // Exemplo: - Type: exam | Value: 6.0 | Weight: 10.0
                    String type = line.substring(line.indexOf("Type:") + 5, line.indexOf("|")).trim();

                    String valuePart = line.substring(line.indexOf("Value:") + 6, line.indexOf("|", line.indexOf("Value:"))).trim();
                    double value = Double.parseDouble(valuePart);

                    String weightPart = line.substring(line.indexOf("Weight:") + 7).trim();
                    double weight = Double.parseDouble(weightPart);

                    // Instancia a classe filha correta com base no tipo guardado
                    if (type.equalsIgnoreCase("exam")) {
                        currentClass.addAssessment(new Exam(value, weight));
                    } else if (type.equalsIgnoreCase("practical") || type.equalsIgnoreCase("Practical Assignment")) {
                        currentClass.addAssessment(new PracticalAssignment(value, weight));
                    } else if (type.equalsIgnoreCase("assignment")) {
                        currentClass.addAssessment(new Assignment(value, weight));
                    } else if (type.equalsIgnoreCase("seminar")) {
                        currentClass.addAssessment(new Seminar(value, weight));
                    }
                }
            }
            System.out.println("Dados carregados do TXT com sucesso! Total de turmas: " + classes.size());
        } catch (Exception e) {
            System.err.println("Erro ao carregar os dados do TXT: " + e.getMessage());
        }
        return classes;
    }

    @Override
    public String getFormatName() {
        return "TXT";
    }
}
