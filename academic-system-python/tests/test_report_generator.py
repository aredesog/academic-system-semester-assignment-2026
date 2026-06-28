import pytest

from academic_system.model.academic_class import AcademicClass
from academic_system.model.exam import Exam
from academic_system.model.practical_assignment import PracticalAssignment
from academic_system.report.report_generator import ReportGenerator


def make_class_with_assessments():
    c = AcademicClass("CS101", "Intro to CS")
    c.add_assessment(Exam(8.0, 0.6))
    c.add_assessment(PracticalAssignment(7.0, 0.4))
    return c


class TestReportGenerator:
    def test_summary_report_contains_class_code(self):
        classes = [make_class_with_assessments()]
        report = ReportGenerator.class_assessment_summary_report(classes)
        assert "CS101" in report

    def test_summary_report_empty_classes(self):
        report = ReportGenerator.class_assessment_summary_report([])
        assert "Nenhuma" in report

    def test_weight_report_valid_weights(self):
        classes = [make_class_with_assessments()]
        report = ReportGenerator.assessment_weight_report(classes)
        assert "valido" in report

    def test_weight_report_invalid_weights(self):
        c = AcademicClass("CS202", "Advanced")
        c.add_assessment(Exam(8.0, 0.5))
        report = ReportGenerator.assessment_weight_report([c])
        assert "invalido" in report

    def test_weight_report_empty_classes(self):
        report = ReportGenerator.assessment_weight_report([])
        assert "Nenhuma" in report

    def test_persistence_config_report(self):
        report = ReportGenerator.persistence_configuration_report("JSON")
        assert "JSON" in report
        assert "Relatorio" in report

    def test_summary_report_contains_assessment_types(self):
        classes = [make_class_with_assessments()]
        report = ReportGenerator.class_assessment_summary_report(classes)
        assert "Exame" in report or "Exam" in report
