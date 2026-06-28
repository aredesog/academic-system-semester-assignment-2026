import logging
import os
from typing import List

from ..model.academic_class import AcademicClass
from ..model.persistence_type import PersistenceType
from ..report.report_generator import ReportGenerator
from ..repository.json_repository import JsonRepository
from ..repository.persistence_strategy import PersistenceStrategy
from ..repository.txt_repository import TxtRepository
from ..repository.xml_repository import XmlRepository

logger = logging.getLogger(__name__)

_CONFIG_FILE = "persistence_config.txt"


class PersistenceService:
    def __init__(self):
        self._current_strategy: PersistenceStrategy = None
        self._configure(self._load_configured_type(), save=False)

    # Ponto de entrada público para trocar o tipo de persistência e salvar a nova configuração
    def configure_persistence_type(self, persistence_type: PersistenceType) -> None:
        self._configure(persistence_type, save=True)

    # Define a estratégia ativa e opcionalmente grava a escolha no arquivo de configuração
    def _configure(self, persistence_type: PersistenceType, save: bool) -> None:
        if persistence_type is None:
            raise ValueError("Tipo de persistencia nao pode ser nulo.")
        if persistence_type == PersistenceType.TXT:
            self._current_strategy = TxtRepository()
        elif persistence_type == PersistenceType.JSON:
            self._current_strategy = JsonRepository()
        elif persistence_type == PersistenceType.XML:
            self._current_strategy = XmlRepository()
        logger.info("Persistence type configured to: %s", persistence_type)
        if save:
            self._save_configured_type(persistence_type)

    # Delega o salvamento das turmas para a estratégia de persistência atualmente ativa
    def save(self, classes: List[AcademicClass]) -> None:
        logger.info("Saving %d class(es) using %s persistence", len(classes), self._current_strategy.get_format_name())
        self._current_strategy.save(classes)
        logger.info("Data saved successfully using %s format", self._current_strategy.get_format_name())

    # Delega o carregamento das turmas para a estratégia de persistência atualmente ativa
    def load(self) -> List[AcademicClass]:
        logger.info("Loading data using %s persistence", self._current_strategy.get_format_name())
        classes = self._current_strategy.load()
        logger.info("Loaded %d class(es)", len(classes))
        return classes

    # Retorna o nome do formato de persistência atualmente configurado
    def get_current_format_name(self) -> str:
        return self._current_strategy.get_format_name() if self._current_strategy else "NENHUM"

    # Gera relatório textual indicando qual tipo de persistência está ativo
    def generate_configuration_report(self) -> str:
        logger.info("Generating persistence configuration report. Active format: %s", self.get_current_format_name())
        return ReportGenerator.persistence_configuration_report(self.get_current_format_name())

    # Lê o tipo de persistência salvo no arquivo de configuração; retorna TXT como padrão
    def _load_configured_type(self) -> PersistenceType:
        if not os.path.exists(_CONFIG_FILE):
            return PersistenceType.TXT
        try:
            with open(_CONFIG_FILE, "r") as f:
                content = f.read().strip()
            return PersistenceType[content.upper()] if content else PersistenceType.TXT
        except (KeyError, Exception):
            return PersistenceType.TXT

    # Grava o tipo de persistência escolhido no arquivo de configuração para recuperação futura
    def _save_configured_type(self, persistence_type: PersistenceType) -> None:
        with open(_CONFIG_FILE, "w") as f:
            f.write(persistence_type.name)
