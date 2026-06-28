package org.example.academic.system.repository;

import org.example.academic.system.model.AcademicClass;
import org.example.academic.system.model.Exam;
import org.example.academic.system.model.Seminar;
import org.junit.jupiter.api.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("US-2389 - Test persistence repositories")
class RepositoryTest {

    private static final String TXT_FILE = "academic_data.txt";
    private static final String JSON_FILE = "academic_data.json";
    private static final String XML_FILE = "academic_data.xml";

    private List<AcademicClass> sampleClasses() {
        AcademicClass c = new AcademicClass("CS101", "Intro to CS");
        c.addAssessment(new Exam(10.0, 0.6));
        c.addAssessment(new Seminar(5.0, 0.4));
        return List.of(c);
    }

    @AfterEach
    void cleanup() throws Exception {
        Files.deleteIfExists(Path.of(TXT_FILE));
        Files.deleteIfExists(Path.of(JSON_FILE));
        Files.deleteIfExists(Path.of(XML_FILE));
    }

    // --- TXT ---

    @Test
    @DisplayName("TXT repository must generate a TXT file")
    void testTxtRepositoryGeneratesFile() {
        new TxtRepository().save(sampleClasses());
        assertTrue(new File(TXT_FILE).exists());
    }

    @Test
    @DisplayName("TXT file must contain class code and title")
    void testTxtFileContainsClassData() throws Exception {
        new TxtRepository().save(sampleClasses());
        String content = Files.readString(Path.of(TXT_FILE));
        assertTrue(content.contains("CS101"));
        assertTrue(content.contains("Intro to CS"));
    }

    @Test
    @DisplayName("TXT file must contain assessment data")
    void testTxtFileContainsAssessmentData() throws Exception {
        new TxtRepository().save(sampleClasses());
        String content = Files.readString(Path.of(TXT_FILE));
        assertTrue(content.contains("Exam") || content.contains("exam"));
        assertTrue(content.contains("10.0"));
        assertTrue(content.contains("0.6"));
    }

    @Test
    @DisplayName("TXT repository getFormatName returns TXT")
    void testTxtFormatName() {
        assertEquals("TXT", new TxtRepository().getFormatName());
    }

    // --- JSON ---

    @Test
    @DisplayName("JSON repository must generate a JSON file")
    void testJsonRepositoryGeneratesFile() {
        new JsonRepository().save(sampleClasses());
        assertTrue(new File(JSON_FILE).exists());
    }

    @Test
    @DisplayName("JSON file must contain class code and title")
    void testJsonFileContainsClassData() throws Exception {
        new JsonRepository().save(sampleClasses());
        String content = Files.readString(Path.of(JSON_FILE));
        assertTrue(content.contains("CS101"));
        assertTrue(content.contains("Intro to CS"));
    }

    @Test
    @DisplayName("JSON file must contain assessment data")
    void testJsonFileContainsAssessmentData() throws Exception {
        new JsonRepository().save(sampleClasses());
        String content = Files.readString(Path.of(JSON_FILE));
        assertTrue(content.contains("Exam") || content.contains("exam"));
        assertTrue(content.contains("10.0"));
        assertTrue(content.contains("0.6"));
    }

    @Test
    @DisplayName("JSON repository getFormatName returns JSON")
    void testJsonFormatName() {
        assertEquals("JSON", new JsonRepository().getFormatName());
    }

    // --- XML ---

    @Test
    @DisplayName("XML repository must generate an XML file")
    void testXmlRepositoryGeneratesFile() {
        new XmlRepository().save(sampleClasses());
        assertTrue(new File(XML_FILE).exists());
    }

    @Test
    @DisplayName("XML file must contain class code and title")
    void testXmlFileContainsClassData() throws Exception {
        new XmlRepository().save(sampleClasses());
        String content = Files.readString(Path.of(XML_FILE));
        assertTrue(content.contains("CS101"));
        assertTrue(content.contains("Intro to CS"));
    }

    @Test
    @DisplayName("XML file must contain assessment data")
    void testXmlFileContainsAssessmentData() throws Exception {
        new XmlRepository().save(sampleClasses());
        String content = Files.readString(Path.of(XML_FILE));
        assertTrue(content.contains("Exam") || content.contains("exam"));
        assertTrue(content.contains("10.0"));
        assertTrue(content.contains("0.6"));
    }

    @Test
    @DisplayName("XML repository getFormatName returns XML")
    void testXmlFormatName() {
        assertEquals("XML", new XmlRepository().getFormatName());
    }
}
