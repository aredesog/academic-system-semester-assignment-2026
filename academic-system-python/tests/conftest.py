import pytest

from academic_system.model.academic_system import AcademicSystem
from academic_system.security.session import Session


@pytest.fixture(autouse=True)
def reset_singletons():
    AcademicSystem.reset()
    Session.reset()
    yield
    AcademicSystem.reset()
    Session.reset()
