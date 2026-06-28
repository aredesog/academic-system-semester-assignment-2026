from ..exception.academic_system_exception import AcademicSystemException
from ..model.academic_system import AcademicSystem
from ..model.assignment import Assignment
from ..model.exam import Exam
from ..model.practical_assignment import PracticalAssignment
from ..model.seminar import Seminar


class AssessmentService:
    def __init__(self, academic_system: AcademicSystem):
        self._academic_system = academic_system

    def register_assessment(
        self, class_code: str, assessment_type: str, value: float, weight: float
    ) -> None:
        academic_class = self._academic_system.find_by_code(class_code)
        if academic_class is None:
            raise AcademicSystemException(f"Turma com codigo '{class_code}' nao encontrada.")
        assessment = self._create_assessment(assessment_type, value, weight)
        academic_class.add_assessment(assessment)

    def _create_assessment(self, assessment_type: str, value: float, weight: float):
        if assessment_type is None:
            raise AcademicSystemException("Tipo de avaliacao nao pode ser nulo.")
        t = assessment_type.lower().strip()
        if t == "exam":
            return Exam(value, weight)
        elif t in ("practical", "practical assignment"):
            return PracticalAssignment(value, weight)
        elif t == "seminar":
            return Seminar(value, weight)
        elif t == "assignment":
            return Assignment(value, weight)
        raise AcademicSystemException(f"Tipo de avaliacao invalido: {assessment_type}")
