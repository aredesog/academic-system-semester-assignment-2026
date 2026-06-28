import logging

from ..exception.authentication_exception import AuthenticationException
from ..model.user import User
from ..repository.user_repository import UserRepository
from .session import Session

logger = logging.getLogger(__name__)


class AuthenticationService:
    def __init__(self, user_repository: UserRepository):
        self._user_repository = user_repository
        self._session = Session.get_instance()

    # Valida as credenciais e abre a sessão se corretas; lança exceção se inválidas
    def authenticate(self, username: str, password: str) -> User:
        user = self._user_repository.find_by_username(username)

        if user is None or user.password != password:
            logger.warning("Failed authentication attempt for username: %s", username)
            raise AuthenticationException("Invalid credentials")

        self._session.login(user)
        logger.info("User '%s' authenticated successfully with role: %s", username, user.role)
        return user

    # Encerra a sessão do usuário autenticado, limpando o estado da sessão
    def logout(self) -> None:
        user = self._session.get_authenticated_user()
        if user:
            logger.info("User '%s' logged out", user.username)
        self._session.logout()
