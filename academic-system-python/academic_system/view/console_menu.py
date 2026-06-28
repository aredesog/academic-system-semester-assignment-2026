from ..controller.academic_system_controller import AcademicSystemController
from ..controller.authentication_controller import AuthenticationController
from ..exception.academic_system_exception import AcademicSystemException
from ..exception.authentication_exception import AuthenticationException
from ..exception.authorization_exception import AuthorizationException
from ..exception.invalid_menu_option_exception import InvalidMenuOptionException
from ..exception.invalid_numeric_input_exception import InvalidNumericInputException
from ..model.persistence_type import PersistenceType


class ConsoleMenu:
    def __init__(
        self,
        academic_controller: AcademicSystemController,
        authentication_controller: AuthenticationController,
    ):
        self._academic_controller = academic_controller
        self._authentication_controller = authentication_controller

    def start(self) -> None:
        while True:
            user = self._login()
            if user.role.name == "ADMIN":
                should_exit = self._show_admin_menu()
            else:
                should_exit = self._show_professor_menu()
            if should_exit:
                break

    def _login(self):
        while True:
            print("\n===== Login =====")
            username = input("Username: ").strip()
            password = input("Password: ").strip()
            try:
                return self._authentication_controller.login(username, password)
            except AuthenticationException as e:
                print(f"Erro de autenticacao: {e}")

    def _show_admin_menu(self) -> bool:
        while True:
            print("\n===== Menu ADMIN =====")
            print("1. Cadastrar turma")
            print("2. Cadastrar avaliacao")
            print("3. Listar turmas")
            print("4. Relatorio de avaliacoes por turma")
            print("5. Relatorio de peso das avaliacoes")
            print("6. Configurar tipo de persistencia")
            print("7. Salvar dados academicos")
            print("8. Relatorio de configuracao de persistencia")
            print("9. Logout")
            print("0. Sair")
            try:
                option = self._read_int("Opcao: ")
                if option == 1:
                    self._register_class()
                elif option == 2:
                    self._register_assessment()
                elif option == 3:
                    self._list_classes()
                elif option == 4:
                    print(self._academic_controller.generate_class_assessment_summary_report())
                elif option == 5:
                    print(self._academic_controller.generate_assessment_weight_report())
                elif option == 6:
                    self._configure_persistence()
                elif option == 7:
                    self._academic_controller.save_academic_data()
                    print("Dados salvos com sucesso.")
                elif option == 8:
                    print(self._academic_controller.generate_persistence_configuration_report())
                elif option == 9:
                    self._authentication_controller.logout()
                    print("Logout realizado com sucesso.")
                    return False
                elif option == 0:
                    return True
                else:
                    raise InvalidMenuOptionException("Opcao invalida.")
            except (AcademicSystemException, AuthorizationException) as e:
                print(f"Erro: {e}")
            except InvalidMenuOptionException as e:
                print(f"Opcao invalida: {e}")
            except InvalidNumericInputException as e:
                print(f"Entrada invalida: {e}")

    def _show_professor_menu(self) -> bool:
        while True:
            print("\n===== Menu PROFESSOR =====")
            print("1. Cadastrar avaliacao")
            print("2. Listar turmas")
            print("3. Relatorio de avaliacoes por turma")
            print("4. Relatorio de peso das avaliacoes")
            print("5. Logout")
            print("0. Sair")
            try:
                option = self._read_int("Opcao: ")
                if option == 1:
                    self._register_assessment()
                elif option == 2:
                    self._list_classes()
                elif option == 3:
                    print(self._academic_controller.generate_class_assessment_summary_report())
                elif option == 4:
                    print(self._academic_controller.generate_assessment_weight_report())
                elif option == 5:
                    self._authentication_controller.logout()
                    print("Logout realizado com sucesso.")
                    return False
                elif option == 0:
                    return True
                else:
                    raise InvalidMenuOptionException("Opcao invalida.")
            except (AcademicSystemException, AuthorizationException) as e:
                print(f"Erro: {e}")
            except InvalidMenuOptionException as e:
                print(f"Opcao invalida: {e}")
            except InvalidNumericInputException as e:
                print(f"Entrada invalida: {e}")

    def _register_class(self) -> None:
        print("\n--- Cadastrar Turma ---")
        code = input("Codigo: ").strip()
        title = input("Titulo: ").strip()
        self._academic_controller.register_class(code, title)
        print("Turma cadastrada com sucesso.")

    def _register_assessment(self) -> None:
        print("\n--- Cadastrar Avaliacao ---")
        class_code = input("Codigo da turma: ").strip()
        print("Tipos: exam, practical assignment, seminar, assignment")
        assessment_type = input("Tipo: ").strip()
        value = self._read_float("Valor: ")
        weight = self._read_float("Peso: ")
        self._academic_controller.register_assessment(class_code, assessment_type, value, weight)
        print("Avaliacao cadastrada com sucesso.")

    def _list_classes(self) -> None:
        classes = self._academic_controller.list_classes()
        if not classes:
            print("Nenhuma turma cadastrada.")
            return
        for c in classes:
            print(f"  {c.code} - {c.title} ({len(c.assessments)} avaliacao(oes))")

    def _configure_persistence(self) -> None:
        print("\n--- Configurar Persistencia ---")
        print("1. TXT  2. JSON  3. XML")
        option = self._read_int("Opcao: ")
        mapping = {1: PersistenceType.TXT, 2: PersistenceType.JSON, 3: PersistenceType.XML}
        if option not in mapping:
            raise InvalidMenuOptionException("Opcao invalida.")
        self._academic_controller.configure_persistence(mapping[option])
        print("Persistencia configurada com sucesso.")

    def _read_int(self, prompt: str) -> int:
        try:
            return int(input(prompt).strip())
        except ValueError:
            raise InvalidNumericInputException("Digite um numero inteiro.")

    def _read_float(self, prompt: str) -> float:
        try:
            return float(input(prompt).strip().replace(",", "."))
        except ValueError:
            raise InvalidNumericInputException("Digite um numero valido.")
