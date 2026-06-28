import logging
import os

from academic_system.model.academic_system import AcademicSystem
from academic_system.repository.txt_user_repository import TxtUserRepository
from academic_system.security.authentication_service import AuthenticationService
from academic_system.security.authorization_service import AuthorizationService
from academic_system.service.assessment_service import AssessmentService
from academic_system.service.class_service import ClassService
from academic_system.service.persistence_service import PersistenceService
from academic_system.service.report_service import ReportService
from academic_system.controller.academic_system_controller import AcademicSystemController
from academic_system.controller.authentication_controller import AuthenticationController
from academic_system.view.console_menu import ConsoleMenu


def setup_logging():
    os.makedirs("logs", exist_ok=True)
    logging.basicConfig(
        level=logging.INFO,
        format="%(asctime)s [%(threadName)s] %(levelname)-5s %(name)s - %(message)s",
        handlers=[
            logging.StreamHandler(),
            logging.FileHandler("logs/academic-system.log", encoding="utf-8"),
        ],
    )


def main():
    setup_logging()

    academic_system = AcademicSystem.get_instance()
    user_repository = TxtUserRepository()

    authentication_service = AuthenticationService(user_repository)
    authorization_service = AuthorizationService()
    persistence_service = PersistenceService()
    class_service = ClassService(academic_system)
    assessment_service = AssessmentService(academic_system)
    report_service = ReportService(academic_system)

    try:
        saved_classes = persistence_service.load()
        academic_system.replace_classes(saved_classes)
    except Exception:
        pass

    academic_controller = AcademicSystemController(
        class_service,
        assessment_service,
        report_service,
        academic_system,
        persistence_service,
        authorization_service,
    )
    authentication_controller = AuthenticationController(authentication_service)

    menu = ConsoleMenu(academic_controller, authentication_controller)
    menu.start()


if __name__ == "__main__":
    main()
