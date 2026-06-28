from abc import ABC, abstractmethod

from ..exception.academic_system_exception import AcademicSystemException


class Assessment(ABC):
    def __init__(self, value: float, weight: float):
        if value < 0:
            raise AcademicSystemException("O valor da avaliacao nao pode ser negativo.")
        if weight <= 0:
            raise AcademicSystemException("O peso da avaliacao deve ser maior que zero.")
        self._value = value
        self._weight = weight

    # Retorna a nota da avaliação
    @property
    def value(self) -> float:
        return self._value

    # Retorna o peso da avaliação no cálculo final
    @property
    def weight(self) -> float:
        return self._weight

    # Método abstrato que cada subclasse implementa para informar seu tipo
    @property
    @abstractmethod
    def type(self) -> str:
        pass

    # Representação textual mostrando tipo, valor e peso
    def __str__(self) -> str:
        return f"{self.type}(value={self._value}, weight={self._weight})"
