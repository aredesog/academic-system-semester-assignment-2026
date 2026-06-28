from .keyboard_input_exception import KeyboardInputException


class InvalidMenuOptionException(KeyboardInputException):
    def __init__(self, message: str):
        super().__init__(message)
