from typing import List

from ..model.academic_class import AcademicClass
from ..model.academic_system import AcademicSystem
from ..model.persistence_type import PersistenceType
from ..model.role import Role
from ..security.authorization_service import AuthorizationService
from ..service.assessment_service import AssessmentService
from ..service.class_service import ClassService
from ..service.persistence_service import PersistenceService
from ..service.report_service import ReportService


class AcademicSystemController:
    def __init__(
        self,
        class_service: ClassService,
        assessment_service: AssessmentService,
        report_service: ReportService,
        academic_system: AcademicSystem,
        persistence_service: PersistenceService,
        authorization_service: AuthorizationService,
    ):
        self._class_service = class_service
        self._assessment_service = assessment_service
        self._report_service = report_service
        self._academic_system = academic_system
        self._persistence_service = persistence_service
        self._authorization_service = authorization_service

    def register_class(self, code: str, title: str) -> None:
        self._authorization_service.authorize(Role.ADMIN)
        self._class_service.register_class(code, title)
        self._persistence_service.save(self._academic_system.get_classes())

    def register_assessment(
        self, class_code: str, assessment_type: str, value: float, weight: float
    ) -> None:
        self._authorization_service.authorize(Role.ADMIN, Role.PROFESSOR)
        self._assessment_service.register_assessment(class_code, assessment_type, value, weight)
        self._persistence_service.save(self._academic_system.get_classes())

    def list_classes(self) -> List[AcademicClass]:
        self._authorization_service.authorize(Role.ADMIN, Role.PROFESSOR)
        return self._academic_system.get_classes()

    def generate_class_assessment_summary_report(self) -> str:
        self._authorization_service.authorize(Role.ADMIN, Role.PROFESSOR)
        return self._report_service.generate_class_assessment_summary_report()

    def generate_assessment_weight_report(self) -> str:
        self._authorization_service.authorize(Role.ADMIN, Role.PROFESSOR)
        return self._report_service.generate_assessment_weight_report()

    def configure_persistence(self, persistence_type: PersistenceType) -> None:
        self._authorization_service.authorize(Role.ADMIN)
        self._persistence_service.configure_persistence_type(persistence_type)
        self._academic_system.replace_classes(self._persistence_service.load())

    def save_academic_data(self) -> None:
        self._authorization_service.authorize(Role.ADMIN)
        self._persistence_service.save(self._academic_system.get_classes())

    def generate_persistence_configuration_report(self) -> str:
        self._authorization_service.authorize(Role.ADMIN)
        return self._persistence_service.generate_configuration_report()
