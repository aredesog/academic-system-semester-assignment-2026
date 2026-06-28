from ..model.user import User
from ..security.authentication_service import AuthenticationService


class AuthenticationController:
    def __init__(self, authentication_service: AuthenticationService):
        self._authentication_service = authentication_service

    # Autentica o usuário com as credenciais fornecidas e retorna o usuário logado
    def login(self, username: str, password: str) -> User:
        return self._authentication_service.authenticate(username, password)

    # Encerra a sessão do usuário autenticado
    def logout(self) -> None:
        self._authentication_service.logout()
