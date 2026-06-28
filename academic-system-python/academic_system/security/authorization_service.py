import logging

from ..exception.authorization_exception import AuthorizationException
from ..model.role import Role
from .session import Session

logger = logging.getLogger(__name__)


class AuthorizationService:
    def __init__(self):
        self._session = Session.get_instance()

    def authorize(self, *required_roles: Role) -> None:
        user = self._session.get_authenticated_user()

        if user is None:
            logger.warning("Authorization failed: no authenticated user")
            raise AuthorizationException("No user is authenticated")

        if user.role not in required_roles:
            logger.warning(
                "Authorization failed: user '%s' with role '%s' attempted operation requiring roles: %s",
                user.username, user.role, required_roles,
            )
            raise AuthorizationException("User does not have the required role")

        logger.debug(
            "Authorization granted for user '%s' with role '%s'",
            user.username, user.role,
        )
