from abc import ABC, abstractmethod
from typing import List

from ..model.academic_class import AcademicClass


class PersistenceStrategy(ABC):
    @abstractmethod
    def save(self, classes: List[AcademicClass]) -> None:
        pass

    @abstractmethod
    def load(self) -> List[AcademicClass]:
        pass

    @abstractmethod
    def get_format_name(self) -> str:
        pass
