from .security_exception import SecurityException


class AuthenticationException(SecurityException):
    def __init__(self, message: str):
        super().__init__(message)
