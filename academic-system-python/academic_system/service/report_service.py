import logging

from ..model.academic_system import AcademicSystem
from ..report.report_generator import ReportGenerator
from ..security.session import Session

logger = logging.getLogger(__name__)


class ReportService:
    def __init__(self, academic_system: AcademicSystem):
        self._academic_system = academic_system

    def generate_class_assessment_summary_report(self) -> str:
        logger.info("Generating class assessment summary report for role: %s", self._current_role())
        report = ReportGenerator.class_assessment_summary_report(self._academic_system.get_classes())
        logger.info("Class assessment summary report generated successfully")
        return report

    def generate_assessment_weight_report(self) -> str:
        logger.info("Generating assessment weight report for role: %s", self._current_role())
        report = ReportGenerator.assessment_weight_report(self._academic_system.get_classes())
        logger.info("Assessment weight report generated successfully")
        return report

    def _current_role(self) -> str:
        user = Session.get_instance().get_authenticated_user()
        return str(user.role) if user else "UNKNOWN"
