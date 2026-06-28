from .assessment import Assessment


class Exam(Assessment):
    # Cria uma avaliação do tipo Exame com valor e peso informados
    def __init__(self, value: float, weight: float):
        super().__init__(value, weight)

    # Retorna o identificador do tipo desta avaliação
    @property
    def type(self) -> str:
        return "Exam"
