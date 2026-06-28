import os
from typing import Optional

from .user_repository import UserRepository
from ..model.role import Role
from ..model.user import User


class TxtUserRepository(UserRepository):
    FILE_NAME = "users.txt"

    def __init__(self):
        self._users: dict[str, User] = self._load_users()

    def _load_users(self) -> dict:
        users = {}
        if not os.path.exists(self.FILE_NAME):
            users["admin"] = User("admin", "admin", Role.ADMIN)
            users["professor"] = User("professor", "professor", Role.PROFESSOR)
            return users
        try:
            with open(self.FILE_NAME, "r", encoding="utf-8") as f:
                for line in f:
                    line = line.strip()
                    if not line:
                        continue
                    parts = line.split(",")
                    if len(parts) == 3:
                        username, password, role_str = parts
                        role = Role[role_str.strip().upper()]
                        users[username.strip()] = User(username.strip(), password.strip(), role)
        except Exception as e:
            print(f"Erro ao carregar usuarios: {e}")
        return users

    def find_by_username(self, username: str) -> Optional[User]:
        return self._users.get(username)
