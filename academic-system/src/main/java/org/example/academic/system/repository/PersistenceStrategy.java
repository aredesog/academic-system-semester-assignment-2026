package org.example.academic.system.repository;

import org.example.academic.system.model.AcademicClass;
import java.util.List;

public interface PersistenceStrategy {
    void save(List<AcademicClass> classes);
    List<AcademicClass> load(); // ADICIONE ESTA LINHA
    String getFormatName();
}