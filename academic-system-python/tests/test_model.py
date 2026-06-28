import pytest

from academic_system.exception.academic_system_exception import AcademicSystemException
from academic_system.model.academic_class import AcademicClass
from academic_system.model.academic_system import AcademicSystem
from academic_system.model.assignment import Assignment
from academic_system.model.exam import Exam
from academic_system.model.practical_assignment import PracticalAssignment
from academic_system.model.role import Role
from academic_system.model.seminar import Seminar
from academic_system.model.user import User


class TestUser:
    def test_create_user_successfully(self):
        user = User("alice", "pass", Role.ADMIN)
        assert user.username == "alice"
        assert user.password == "pass"
        assert user.role == Role.ADMIN

    def test_equality_by_username(self):
        u1 = User("alice", "pass1", Role.ADMIN)
        u2 = User("alice", "pass2", Role.PROFESSOR)
        assert u1 == u2

    def test_different_username_not_equal(self):
        u1 = User("alice", "pass", Role.ADMIN)
        u2 = User("bob", "pass", Role.ADMIN)
        assert u1 != u2

    def test_null_username_raises(self):
        with pytest.raises(ValueError):
            User(None, "pass", Role.ADMIN)

    def test_null_password_raises(self):
        with pytest.raises(ValueError):
            User("alice", None, Role.ADMIN)

    def test_null_role_raises(self):
        with pytest.raises(ValueError):
            User("alice", "pass", None)


class TestAssessments:
    def test_exam_creation(self):
        exam = Exam(8.5, 0.4)
        assert exam.value == 8.5
        assert exam.weight == 0.4
        assert exam.type == "Exam"

    def test_practical_assignment_creation(self):
        pa = PracticalAssignment(7.0, 0.3)
        assert pa.type == "Practical Assignment"

    def test_seminar_creation(self):
        s = Seminar(9.0, 0.2)
        assert s.type == "Seminar"

    def test_assignment_creation(self):
        a = Assignment(6.0, 0.1)
        assert a.type == "Assignment"

    def test_negative_value_raises(self):
        with pytest.raises(AcademicSystemException):
            Exam(-1.0, 0.5)

    def test_zero_weight_raises(self):
        with pytest.raises(AcademicSystemException):
            Exam(7.0, 0.0)

    def test_negative_weight_raises(self):
        with pytest.raises(AcademicSystemException):
            Exam(7.0, -0.5)


class TestAcademicClass:
    def test_create_class_successfully(self):
        c = AcademicClass("CS101", "Intro to CS")
        assert c.code == "CS101"
        assert c.title == "Intro to CS"
        assert c.assessments == []

    def test_add_assessment(self):
        c = AcademicClass("CS101", "Intro to CS")
        c.add_assessment(Exam(8.0, 1.0))
        assert len(c.assessments) == 1

    def test_assessments_returns_copy(self):
        c = AcademicClass("CS101", "Intro to CS")
        c.add_assessment(Exam(8.0, 1.0))
        lst = c.assessments
        lst.clear()
        assert len(c.assessments) == 1

    def test_equality_by_code(self):
        c1 = AcademicClass("CS101", "Intro to CS")
        c2 = AcademicClass("CS101", "Different Title")
        assert c1 == c2

    def test_empty_code_raises(self):
        with pytest.raises(AcademicSystemException):
            AcademicClass("", "Title")

    def test_empty_title_raises(self):
        with pytest.raises(AcademicSystemException):
            AcademicClass("CS101", "")


class TestAcademicSystem:
    def test_singleton_same_instance(self):
        a1 = AcademicSystem.get_instance()
        a2 = AcademicSystem.get_instance()
        assert a1 is a2

    def test_add_and_find_class(self):
        system = AcademicSystem.get_instance()
        system.add_class(AcademicClass("CS101", "Intro"))
        found = system.find_by_code("CS101")
        assert found is not None
        assert found.code == "CS101"

    def test_find_nonexistent_returns_none(self):
        system = AcademicSystem.get_instance()
        assert system.find_by_code("NONE") is None

    def test_replace_classes(self):
        system = AcademicSystem.get_instance()
        system.add_class(AcademicClass("CS101", "Intro"))
        new_classes = [AcademicClass("CS202", "Advanced")]
        system.replace_classes(new_classes)
        assert len(system.get_classes()) == 1
        assert system.get_classes()[0].code == "CS202"
