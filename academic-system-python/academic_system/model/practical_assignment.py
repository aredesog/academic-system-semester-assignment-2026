from .assessment import Assessment


class PracticalAssignment(Assessment):
    # Cria uma avaliação do tipo Atividade Prática com valor e peso informados
    def __init__(self, value: float, weight: float):
        super().__init__(value, weight)

    # Retorna o identificador do tipo desta avaliação
    @property
    def type(self) -> str:
        return "Practical Assignment"
