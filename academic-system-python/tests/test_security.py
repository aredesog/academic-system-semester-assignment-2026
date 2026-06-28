import pytest

from academic_system.exception.authentication_exception import AuthenticationException
from academic_system.exception.authorization_exception import AuthorizationException
from academic_system.exception.security_exception import SecurityException
from academic_system.model.role import Role
from academic_system.model.user import User
from academic_system.security.authentication_service import AuthenticationService
from academic_system.security.authorization_service import AuthorizationService
from academic_system.security.session import Session


def make_user_repo(user=None):
    from unittest.mock import MagicMock
    repo = MagicMock()
    repo.find_by_username.return_value = user
    return repo


class TestAuthenticationService:
    def test_successful_authentication(self):
        user = User("admin", "admin", Role.ADMIN)
        service = AuthenticationService(make_user_repo(user))
        result = service.authenticate("admin", "admin")
        assert result == user

    def test_sets_session_on_login(self):
        user = User("admin", "admin", Role.ADMIN)
        service = AuthenticationService(make_user_repo(user))
        service.authenticate("admin", "admin")
        assert Session.get_instance().get_authenticated_user() == user

    def test_wrong_password_raises(self):
        user = User("admin", "admin", Role.ADMIN)
        service = AuthenticationService(make_user_repo(user))
        with pytest.raises(AuthenticationException):
            service.authenticate("admin", "wrong")

    def test_unknown_user_raises(self):
        service = AuthenticationService(make_user_repo(None))
        with pytest.raises(AuthenticationException):
            service.authenticate("nobody", "pass")

    def test_authentication_exception_is_security_exception(self):
        assert issubclass(AuthenticationException, SecurityException)

    def test_logout_clears_session(self):
        user = User("admin", "admin", Role.ADMIN)
        service = AuthenticationService(make_user_repo(user))
        service.authenticate("admin", "admin")
        service.logout()
        assert Session.get_instance().get_authenticated_user() is None


class TestAuthorizationService:
    def _login(self, role: Role):
        user = User("testuser", "pass", role)
        Session.get_instance().login(user)

    def test_authorized_role_passes(self):
        self._login(Role.ADMIN)
        service = AuthorizationService()
        service.authorize(Role.ADMIN)

    def test_unauthorized_role_raises(self):
        self._login(Role.PROFESSOR)
        service = AuthorizationService()
        with pytest.raises(AuthorizationException):
            service.authorize(Role.ADMIN)

    def test_not_logged_in_raises(self):
        service = AuthorizationService()
        with pytest.raises(AuthorizationException):
            service.authorize(Role.ADMIN)

    def test_authorization_exception_is_security_exception(self):
        assert issubclass(AuthorizationException, SecurityException)

    def test_multiple_roles_authorized(self):
        self._login(Role.PROFESSOR)
        service = AuthorizationService()
        service.authorize(Role.ADMIN, Role.PROFESSOR)


class TestSession:
    def test_singleton(self):
        s1 = Session.get_instance()
        s2 = Session.get_instance()
        assert s1 is s2

    def test_initially_not_authenticated(self):
        session = Session.get_instance()
        assert not session.is_authenticated()

    def test_login_and_logout(self):
        session = Session.get_instance()
        user = User("admin", "admin", Role.ADMIN)
        session.login(user)
        assert session.is_authenticated()
        session.logout()
        assert not session.is_authenticated()
