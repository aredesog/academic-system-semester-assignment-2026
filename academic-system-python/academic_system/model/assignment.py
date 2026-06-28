from .assessment import Assessment


class Assignment(Assessment):
    # Cria uma avaliação do tipo Trabalho com valor e peso informados
    def __init__(self, value: float, weight: float):
        super().__init__(value, weight)

    # Retorna o identificador do tipo desta avaliação
    @property
    def type(self) -> str:
        return "Assignment"
