class DomainValidator:
    """
    Architectural equivalent of Jakarta Bean Validation.
    In Python, validation is performed directly in class constructors.
    This class exists to maintain consistency with the Java architecture.
    """

    @staticmethod
    def validate(obj) -> None:
        pass
