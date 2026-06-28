from typing import Optional

from ..model.user import User


class Session:
    _instance: Optional["Session"] = None

    def __init__(self):
        self._authenticated_user: Optional[User] = None

    @classmethod
    def get_instance(cls) -> "Session":
        if cls._instance is None:
            cls._instance = Session()
        return cls._instance

    @classmethod
    def reset(cls) -> None:
        cls._instance = None

    def login(self, user: User) -> None:
        self._authenticated_user = user

    def logout(self) -> None:
        self._authenticated_user = None

    def get_authenticated_user(self) -> Optional[User]:
        return self._authenticated_user

    def is_authenticated(self) -> bool:
        return self._authenticated_user is not None
