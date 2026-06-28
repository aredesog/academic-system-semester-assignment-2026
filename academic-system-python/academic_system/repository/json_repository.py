import json
import os
from typing import List

from .persistence_strategy import PersistenceStrategy
from ..model.academic_class import AcademicClass
from ..model.assignment import Assignment
from ..model.exam import Exam
from ..model.practical_assignment import PracticalAssignment
from ..model.seminar import Seminar


class JsonRepository(PersistenceStrategy):
    FILE_NAME = "academic_data.json"

    # Serializa a lista de turmas para o arquivo JSON com indentação
    def save(self, classes: List[AcademicClass]) -> None:
        try:
            data = [
                {
                    "code": c.code,
                    "title": c.title,
                    "assessments": [
                        {"type": a.type, "value": a.value, "weight": a.weight}
                        for a in c.assessments
                    ],
                }
                for c in classes
            ]
            with open(self.FILE_NAME, "w", encoding="utf-8") as f:
                json.dump(data, f, indent=2, ensure_ascii=False)
        except Exception as e:
            print(f"Erro ao salvar os dados em JSON: {e}")

    # Lê o arquivo JSON e reconstrói a lista de turmas com suas avaliações
    def load(self) -> List[AcademicClass]:
        if not os.path.exists(self.FILE_NAME):
            return []
        try:
            with open(self.FILE_NAME, "r", encoding="utf-8") as f:
                data = json.load(f)
            classes = []
            for class_data in data:
                academic_class = AcademicClass(class_data["code"], class_data["title"])
                for a in class_data.get("assessments", []):
                    academic_class.add_assessment(
                        self._create_assessment(a["type"], a["value"], a["weight"])
                    )
                classes.append(academic_class)
            return classes
        except Exception as e:
            print(f"Erro ao carregar os dados do JSON: {e}")
            return []

    # Instancia a subclasse correta de Assessment com base no tipo lido do JSON
    def _create_assessment(self, type_str: str, value: float, weight: float):
        t = type_str.lower().strip()
        if t == "exam":
            return Exam(value, weight)
        elif t in ("practical", "practical assignment"):
            return PracticalAssignment(value, weight)
        elif t == "seminar":
            return Seminar(value, weight)
        return Assignment(value, weight)

    # Retorna o nome do formato para uso nos logs e relatórios
    def get_format_name(self) -> str:
        return "JSON"
