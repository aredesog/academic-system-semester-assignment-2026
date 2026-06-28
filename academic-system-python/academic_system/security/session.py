from typing import Optional

from ..model.user import User


class Session:
    _instance: Optional["Session"] = None

    def __init__(self):
        self._authenticated_user: Optional[User] = None

    # Retorna a única instância da sessão, criando-a na primeira chamada (Singleton)
    @classmethod
    def get_instance(cls) -> "Session":
        if cls._instance is None:
            cls._instance = Session()
        return cls._instance

    # Reseta o Singleton — usado principalmente nos testes para isolar o estado
    @classmethod
    def reset(cls) -> None:
        cls._instance = None

    # Registra o usuário autenticado na sessão atual
    def login(self, user: User) -> None:
        self._authenticated_user = user

    # Remove o usuário da sessão, efetivando o logout
    def logout(self) -> None:
        self._authenticated_user = None

    # Retorna o usuário autenticado, ou None se ninguém estiver logado
    def get_authenticated_user(self) -> Optional[User]:
        return self._authenticated_user

    # Verifica se existe um usuário autenticado na sessão
    def is_authenticated(self) -> bool:
        return self._authenticated_user is not None
