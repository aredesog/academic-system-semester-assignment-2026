import os
from typing import List

from .persistence_strategy import PersistenceStrategy
from ..model.academic_class import AcademicClass
from ..model.assignment import Assignment
from ..model.exam import Exam
from ..model.practical_assignment import PracticalAssignment
from ..model.seminar import Seminar


class TxtRepository(PersistenceStrategy):
    FILE_NAME = "academic_data.txt"

    # Escreve cada turma e suas avaliações no arquivo TXT em formato legível
    def save(self, classes: List[AcademicClass]) -> None:
        try:
            with open(self.FILE_NAME, "w", encoding="utf-8") as f:
                for academic_class in classes:
                    f.write(f"Class Code: {academic_class.code}\n")
                    f.write(f"Class Title: {academic_class.title}\n")
                    f.write("Assessments:\n")
                    for assessment in academic_class.assessments:
                        f.write(
                            f"  - Type: {assessment.type}"
                            f" | Value: {assessment.value}"
                            f" | Weight: {assessment.weight}\n"
                        )
                    f.write("=========================================\n")
        except Exception as e:
            print(f"Erro ao salvar os dados em TXT: {e}")

    # Lê o arquivo TXT linha a linha e reconstrói as turmas com suas avaliações
    def load(self) -> List[AcademicClass]:
        classes = []
        if not os.path.exists(self.FILE_NAME):
            return classes
        try:
            with open(self.FILE_NAME, "r", encoding="utf-8") as f:
                current_class = None
                for line in f:
                    line = line.strip()
                    if line.startswith("Class Code:"):
                        code = line.replace("Class Code:", "").strip()
                        title_line = next(f, "").strip()
                        title = title_line.replace("Class Title:", "").strip()
                        current_class = AcademicClass(code, title)
                        classes.append(current_class)
                    elif line.startswith("- Type:") and current_class:
                        type_part = line[line.index("Type:") + 5:line.index("|")].strip()
                        value_part = line[line.index("Value:") + 6:line.index("|", line.index("Value:"))].strip()
                        weight_part = line[line.index("Weight:") + 7:].strip()
                        value = float(value_part)
                        weight = float(weight_part)
                        current_class.add_assessment(self._create_assessment(type_part, value, weight))
        except Exception as e:
            print(f"Erro ao carregar os dados do TXT: {e}")
        return classes

    # Instancia a subclasse correta de Assessment com base no tipo lido do TXT
    def _create_assessment(self, type_str: str, value: float, weight: float):
        t = type_str.lower()
        if t == "exam":
            return Exam(value, weight)
        elif t in ("practical", "practical assignment"):
            return PracticalAssignment(value, weight)
        elif t == "seminar":
            return Seminar(value, weight)
        return Assignment(value, weight)

    # Retorna o nome do formato para uso nos logs e relatórios
    def get_format_name(self) -> str:
        return "TXT"
