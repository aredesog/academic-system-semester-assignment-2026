import os
import pytest
from unittest.mock import MagicMock

from academic_system.exception.academic_system_exception import AcademicSystemException
from academic_system.model.academic_class import AcademicClass
from academic_system.model.academic_system import AcademicSystem
from academic_system.model.exam import Exam
from academic_system.service.assessment_service import AssessmentService
from academic_system.service.class_service import ClassService
from academic_system.service.report_service import ReportService


@pytest.fixture
def academic_system():
    return AcademicSystem.get_instance()


@pytest.fixture
def class_service(academic_system):
    return ClassService(academic_system)


@pytest.fixture
def assessment_service(academic_system):
    return AssessmentService(academic_system)


@pytest.fixture
def report_service(academic_system):
    return ReportService(academic_system)


class TestClassService:
    def test_register_class_successfully(self, class_service, academic_system):
        class_service.register_class("CS101", "Intro to CS")
        assert academic_system.find_by_code("CS101") is not None

    def test_register_duplicate_code_raises(self, class_service):
        class_service.register_class("CS101", "Intro to CS")
        with pytest.raises(AcademicSystemException):
            class_service.register_class("CS101", "Duplicate")


class TestAssessmentService:
    def test_register_exam_to_class(self, class_service, assessment_service, academic_system):
        class_service.register_class("CS101", "Intro")
        assessment_service.register_assessment("CS101", "exam", 8.0, 1.0)
        c = academic_system.find_by_code("CS101")
        assert len(c.assessments) == 1
        assert c.assessments[0].type == "Exam"

    def test_register_assessment_unknown_class_raises(self, assessment_service):
        with pytest.raises(AcademicSystemException):
            assessment_service.register_assessment("NONE", "exam", 8.0, 1.0)

    def test_register_invalid_type_raises(self, class_service, assessment_service):
        class_service.register_class("CS101", "Intro")
        with pytest.raises(AcademicSystemException):
            assessment_service.register_assessment("CS101", "unknown_type", 8.0, 1.0)

    def test_register_practical_assignment(self, class_service, assessment_service, academic_system):
        class_service.register_class("CS101", "Intro")
        assessment_service.register_assessment("CS101", "practical assignment", 7.0, 0.5)
        c = academic_system.find_by_code("CS101")
        assert c.assessments[0].type == "Practical Assignment"

    def test_register_seminar(self, class_service, assessment_service, academic_system):
        class_service.register_class("CS101", "Intro")
        assessment_service.register_assessment("CS101", "seminar", 9.0, 0.3)
        c = academic_system.find_by_code("CS101")
        assert c.assessments[0].type == "Seminar"

    def test_register_assignment(self, class_service, assessment_service, academic_system):
        class_service.register_class("CS101", "Intro")
        assessment_service.register_assessment("CS101", "assignment", 6.0, 0.2)
        c = academic_system.find_by_code("CS101")
        assert c.assessments[0].type == "Assignment"


class TestReportService:
    def test_class_assessment_summary_with_classes(self, class_service, assessment_service, report_service):
        class_service.register_class("CS101", "Intro")
        assessment_service.register_assessment("CS101", "exam", 8.0, 1.0)
        report = report_service.generate_class_assessment_summary_report()
        assert "CS101" in report
        assert "Intro" in report

    def test_assessment_weight_report(self, class_service, assessment_service, report_service):
        class_service.register_class("CS101", "Intro")
        assessment_service.register_assessment("CS101", "exam", 8.0, 1.0)
        report = report_service.generate_assessment_weight_report()
        assert "CS101" in report
        assert "1.0" in report

    def test_reports_empty_when_no_classes(self, report_service):
        report = report_service.generate_class_assessment_summary_report()
        assert "Nenhuma" in report
