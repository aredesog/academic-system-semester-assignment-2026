from .assessment import Assessment


class Assignment(Assessment):
    def __init__(self, value: float, weight: float):
        super().__init__(value, weight)

    @property
    def type(self) -> str:
        return "Assignment"
