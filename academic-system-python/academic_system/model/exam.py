from .assessment import Assessment


class Exam(Assessment):
    def __init__(self, value: float, weight: float):
        super().__init__(value, weight)

    @property
    def type(self) -> str:
        return "Exam"
