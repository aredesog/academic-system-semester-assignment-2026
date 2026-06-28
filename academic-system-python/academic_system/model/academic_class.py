from typing import List

from .assessment import Assessment
from ..exception.academic_system_exception import AcademicSystemException


class AcademicClass:
    def __init__(self, code: str, title: str):
        if not code or not code.strip():
            raise AcademicSystemException("O codigo da turma nao pode estar vazio.")
        if not title or not title.strip():
            raise AcademicSystemException("O titulo da turma nao pode estar vazio.")
        self._code = code.strip()
        self._title = title.strip()
        self._assessments: List[Assessment] = []

    # Retorna o código da turma
    @property
    def code(self) -> str:
        return self._code

    # Retorna o título da turma
    @property
    def title(self) -> str:
        return self._title

    # Retorna uma cópia da lista de avaliações para evitar modificações externas
    @property
    def assessments(self) -> List[Assessment]:
        return list(self._assessments)

    # Adiciona uma avaliação à turma, rejeitando valores None
    def add_assessment(self, assessment: Assessment) -> None:
        if assessment is None:
            raise AcademicSystemException("Assessment cannot be None")
        self._assessments.append(assessment)

    # Duas turmas são iguais se tiverem o mesmo código
    def __eq__(self, other) -> bool:
        if not isinstance(other, AcademicClass):
            return False
        return self._code == other._code

    # Hash baseado no código da turma para consistência com __eq__
    def __hash__(self) -> int:
        return hash(self._code)

    # Representação textual da turma
    def __str__(self) -> str:
        return f"AcademicClass(code={self._code}, title={self._title})"
