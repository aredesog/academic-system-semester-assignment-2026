from ..exception.academic_system_exception import AcademicSystemException
from ..model.academic_class import AcademicClass
from ..model.academic_system import AcademicSystem


class ClassService:
    def __init__(self, academic_system: AcademicSystem):
        self._academic_system = academic_system

    # Cria e registra uma nova turma, garantindo que o código ainda não exista no sistema
    def register_class(self, code: str, title: str) -> None:
        if self._academic_system.find_by_code(code):
            raise AcademicSystemException(f"Turma com codigo '{code}' ja existe.")
        academic_class = AcademicClass(code, title)
        self._academic_system.add_class(academic_class)
