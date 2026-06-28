from .security_exception import SecurityException


class AuthorizationException(SecurityException):
    def __init__(self, message: str):
        super().__init__(message)
