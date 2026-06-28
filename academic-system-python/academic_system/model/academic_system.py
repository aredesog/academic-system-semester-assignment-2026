from typing import List, Optional

from .academic_class import AcademicClass


class AcademicSystem:
    _instance: Optional["AcademicSystem"] = None

    def __init__(self):
        self._classes: List[AcademicClass] = []

    # Retorna a única instância do sistema, criando-a na primeira chamada (Singleton)
    @classmethod
    def get_instance(cls) -> "AcademicSystem":
        if cls._instance is None:
            cls._instance = AcademicSystem()
        return cls._instance

    # Reseta o Singleton — usado principalmente nos testes para isolar o estado
    @classmethod
    def reset(cls) -> None:
        cls._instance = None

    # Adiciona uma nova turma à lista interna
    def add_class(self, academic_class: AcademicClass) -> None:
        self._classes.append(academic_class)

    # Busca e retorna uma turma pelo código, ou None se não encontrada
    def find_by_code(self, code: str) -> Optional[AcademicClass]:
        for c in self._classes:
            if c.code == code:
                return c
        return None

    # Retorna uma cópia da lista de turmas para evitar modificações externas
    def get_classes(self) -> List[AcademicClass]:
        return list(self._classes)

    # Substitui todas as turmas pelas fornecidas (usada ao trocar tipo de persistência)
    def replace_classes(self, classes: List[AcademicClass]) -> None:
        self._classes = list(classes)
