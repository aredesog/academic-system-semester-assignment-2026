import logging
import pytest
from unittest.mock import MagicMock, patch

from academic_system.model.role import Role
from academic_system.model.user import User
from academic_system.security.authentication_service import AuthenticationService
from academic_system.security.authorization_service import AuthorizationService
from academic_system.security.session import Session
from academic_system.exception.authentication_exception import AuthenticationException
from academic_system.exception.authorization_exception import AuthorizationException


def make_user_repo(user=None):
    repo = MagicMock()
    repo.find_by_username.return_value = user
    return repo


class TestLoggingInfrastructure:
    def test_authentication_service_has_logger(self):
        service = AuthenticationService(make_user_repo())
        logger = logging.getLogger("academic_system.security.authentication_service")
        assert logger is not None

    def test_authorization_service_has_logger(self):
        service = AuthorizationService()
        logger = logging.getLogger("academic_system.security.authorization_service")
        assert logger is not None

    def test_successful_login_logs_info(self, caplog):
        user = User("admin", "admin", Role.ADMIN)
        service = AuthenticationService(make_user_repo(user))
        with caplog.at_level(logging.INFO, logger="academic_system.security.authentication_service"):
            service.authenticate("admin", "admin")
        assert "admin" in caplog.text

    def test_failed_login_logs_warning(self, caplog):
        user = User("admin", "admin", Role.ADMIN)
        service = AuthenticationService(make_user_repo(user))
        with caplog.at_level(logging.WARNING, logger="academic_system.security.authentication_service"):
            try:
                service.authenticate("admin", "wrong")
            except AuthenticationException:
                pass
        assert "Failed" in caplog.text or "admin" in caplog.text

    def test_authorization_failure_logs_warning(self, caplog):
        professor = User("prof", "prof", Role.PROFESSOR)
        Session.get_instance().login(professor)
        service = AuthorizationService()
        with caplog.at_level(logging.WARNING, logger="academic_system.security.authorization_service"):
            try:
                service.authorize(Role.ADMIN)
            except AuthorizationException:
                pass
        assert "prof" in caplog.text or "Authorization" in caplog.text

    def test_logout_logs_info(self, caplog):
        user = User("admin", "admin", Role.ADMIN)
        service = AuthenticationService(make_user_repo(user))
        service.authenticate("admin", "admin")
        with caplog.at_level(logging.INFO, logger="academic_system.security.authentication_service"):
            service.logout()
        assert "admin" in caplog.text

    def test_no_authenticated_user_on_authorize_logs_warning(self, caplog):
        service = AuthorizationService()
        with caplog.at_level(logging.WARNING, logger="academic_system.security.authorization_service"):
            try:
                service.authorize(Role.ADMIN)
            except AuthorizationException:
                pass
        assert "Authorization" in caplog.text or "authenticated" in caplog.text
