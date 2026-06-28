import os
import xml.etree.ElementTree as ET
from typing import List

from .persistence_strategy import PersistenceStrategy
from ..model.academic_class import AcademicClass
from ..model.assignment import Assignment
from ..model.exam import Exam
from ..model.practical_assignment import PracticalAssignment
from ..model.seminar import Seminar


class XmlRepository(PersistenceStrategy):
    FILE_NAME = "academic_data.xml"

    # Serializa a lista de turmas para o arquivo XML usando a biblioteca padrão ElementTree
    def save(self, classes: List[AcademicClass]) -> None:
        try:
            root = ET.Element("classes")
            for academic_class in classes:
                class_elem = ET.SubElement(root, "class")
                ET.SubElement(class_elem, "code").text = academic_class.code
                ET.SubElement(class_elem, "title").text = academic_class.title
                assessments_elem = ET.SubElement(class_elem, "assessments")
                for assessment in academic_class.assessments:
                    a_elem = ET.SubElement(assessments_elem, "assessment")
                    ET.SubElement(a_elem, "type").text = assessment.type
                    ET.SubElement(a_elem, "value").text = str(assessment.value)
                    ET.SubElement(a_elem, "weight").text = str(assessment.weight)
            tree = ET.ElementTree(root)
            ET.indent(tree, space="  ")
            tree.write(self.FILE_NAME, encoding="unicode", xml_declaration=False)
        except Exception as e:
            print(f"Erro ao salvar os dados em XML: {e}")

    # Lê o arquivo XML e reconstrói a lista de turmas com suas avaliações
    def load(self) -> List[AcademicClass]:
        if not os.path.exists(self.FILE_NAME):
            return []
        try:
            tree = ET.parse(self.FILE_NAME)
            root = tree.getroot()
            classes = []
            for class_elem in root.findall("class"):
                code = class_elem.findtext("code", "")
                title = class_elem.findtext("title", "")
                academic_class = AcademicClass(code, title)
                for a_elem in class_elem.findall("./assessments/assessment"):
                    t = a_elem.findtext("type", "").lower().strip()
                    value = float(a_elem.findtext("value", "0"))
                    weight = float(a_elem.findtext("weight", "0"))
                    academic_class.add_assessment(self._create_assessment(t, value, weight))
                classes.append(academic_class)
            return classes
        except Exception as e:
            print(f"Erro ao carregar os dados do XML: {e}")
            return []

    # Instancia a subclasse correta de Assessment com base no tipo lido do XML
    def _create_assessment(self, type_str: str, value: float, weight: float):
        if type_str == "exam":
            return Exam(value, weight)
        elif type_str in ("practical", "practical assignment"):
            return PracticalAssignment(value, weight)
        elif type_str == "seminar":
            return Seminar(value, weight)
        return Assignment(value, weight)

    # Retorna o nome do formato para uso nos logs e relatórios
    def get_format_name(self) -> str:
        return "XML"
