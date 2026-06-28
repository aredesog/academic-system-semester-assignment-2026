import os
import pytest

from academic_system.model.academic_class import AcademicClass
from academic_system.model.exam import Exam
from academic_system.model.practical_assignment import PracticalAssignment
from academic_system.repository.json_repository import JsonRepository
from academic_system.repository.txt_repository import TxtRepository
from academic_system.repository.xml_repository import XmlRepository


def make_classes():
    c = AcademicClass("CS101", "Intro to CS")
    c.add_assessment(Exam(8.5, 0.6))
    c.add_assessment(PracticalAssignment(7.0, 0.4))
    return [c]


@pytest.fixture(autouse=True)
def cleanup_files():
    yield
    for f in ["academic_data.txt", "academic_data.json", "academic_data.xml"]:
        if os.path.exists(f):
            os.remove(f)


class TestTxtRepository:
    def test_save_and_load(self):
        repo = TxtRepository()
        classes = make_classes()
        repo.save(classes)
        loaded = repo.load()
        assert len(loaded) == 1
        assert loaded[0].code == "CS101"
        assert loaded[0].title == "Intro to CS"
        assert len(loaded[0].assessments) == 2

    def test_load_returns_empty_when_no_file(self):
        repo = TxtRepository()
        result = repo.load()
        assert result == []

    def test_format_name(self):
        assert TxtRepository().get_format_name() == "TXT"

    def test_assessment_types_preserved(self):
        repo = TxtRepository()
        repo.save(make_classes())
        loaded = repo.load()
        types = [a.type for a in loaded[0].assessments]
        assert "Exam" in types
        assert "Practical Assignment" in types


class TestJsonRepository:
    def test_save_and_load(self):
        repo = JsonRepository()
        classes = make_classes()
        repo.save(classes)
        loaded = repo.load()
        assert len(loaded) == 1
        assert loaded[0].code == "CS101"
        assert len(loaded[0].assessments) == 2

    def test_load_returns_empty_when_no_file(self):
        repo = JsonRepository()
        result = repo.load()
        assert result == []

    def test_format_name(self):
        assert JsonRepository().get_format_name() == "JSON"

    def test_values_preserved(self):
        repo = JsonRepository()
        repo.save(make_classes())
        loaded = repo.load()
        exam = next(a for a in loaded[0].assessments if a.type == "Exam")
        assert exam.value == 8.5
        assert exam.weight == 0.6


class TestXmlRepository:
    def test_save_and_load(self):
        repo = XmlRepository()
        classes = make_classes()
        repo.save(classes)
        loaded = repo.load()
        assert len(loaded) == 1
        assert loaded[0].code == "CS101"
        assert len(loaded[0].assessments) == 2

    def test_load_returns_empty_when_no_file(self):
        repo = XmlRepository()
        result = repo.load()
        assert result == []

    def test_format_name(self):
        assert XmlRepository().get_format_name() == "XML"

    def test_assessment_types_preserved(self):
        repo = XmlRepository()
        repo.save(make_classes())
        loaded = repo.load()
        types = [a.type for a in loaded[0].assessments]
        assert "Exam" in types
        assert "Practical Assignment" in types
