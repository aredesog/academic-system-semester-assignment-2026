from .role import Role


class User:
    def __init__(self, username: str, password: str, role: Role):
        if username is None:
            raise ValueError("Username cannot be None")
        if password is None:
            raise ValueError("Password cannot be None")
        if role is None:
            raise ValueError("Role cannot be None")
        self._username = username
        self._password = password
        self._role = role

    # Retorna o nome de usuário
    @property
    def username(self) -> str:
        return self._username

    # Retorna a senha do usuário
    @property
    def password(self) -> str:
        return self._password

    # Retorna o papel do usuário (ADMIN ou PROFESSOR)
    @property
    def role(self) -> Role:
        return self._role

    # Dois usuários são iguais se tiverem o mesmo username
    def __eq__(self, other) -> bool:
        if not isinstance(other, User):
            return False
        return self._username == other._username

    # Hash baseado no username para consistência com __eq__
    def __hash__(self) -> int:
        return hash(self._username)

    # Representação textual do usuário
    def __str__(self) -> str:
        return f"User(username={self._username}, role={self._role})"
