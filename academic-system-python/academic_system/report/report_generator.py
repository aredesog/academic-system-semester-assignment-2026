from typing import List

from ..model.academic_class import AcademicClass

_VALID_WEIGHT_TOLERANCE = 0.0001


class ReportGenerator:

    @staticmethod
    def class_assessment_summary_report(classes: List[AcademicClass]) -> str:
        lines = ["===== Relatorio de Avaliacoes por Turma ====="]

        if not classes:
            lines.append("\nNenhuma turma cadastrada.")
            return "\n".join(lines)

        for academic_class in classes:
            lines.append(f"\nTurma: {academic_class.code} - {academic_class.title}")
            lines.append("Avaliacoes:")
            if not academic_class.assessments:
                lines.append("Nenhuma avaliacao cadastrada.")
                continue
            for assessment in academic_class.assessments:
                lines.append(
                    f"* Tipo: {ReportGenerator._format_type(assessment)}"
                    f" | Valor: {assessment.value}"
                    f" | Peso: {assessment.weight}"
                )

        return "\n".join(lines)

    @staticmethod
    def assessment_weight_report(classes: List[AcademicClass]) -> str:
        lines = ["===== Relatorio de Peso das Avaliacoes ====="]

        if not classes:
            lines.append("\nNenhuma turma cadastrada.")
            return "\n".join(lines)

        for academic_class in classes:
            total_weight = sum(a.weight for a in academic_class.assessments)
            status = "valido" if abs(total_weight - 1.0) < _VALID_WEIGHT_TOLERANCE else "invalido"
            lines.append(f"\nTurma: {academic_class.code} - {academic_class.title}")
            lines.append(f"Peso total: {total_weight}")
            lines.append(f"Status: {status}")

        return "\n".join(lines)

    @staticmethod
    def persistence_configuration_report(format_name: str) -> str:
        return (
            "===== Relatorio de Configuracao de Persistencia =====\n"
            f"Tipo de persistencia ativo: {format_name}"
        )

    @staticmethod
    def _format_type(assessment) -> str:
        type_map = {
            "Exam": "Exame",
            "Practical Assignment": "Atividade pratica",
            "Seminar": "Seminario",
            "Assignment": "Trabalho",
        }
        return type_map.get(assessment.type, assessment.type)
