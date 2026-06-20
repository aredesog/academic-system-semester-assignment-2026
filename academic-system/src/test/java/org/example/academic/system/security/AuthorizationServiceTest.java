package org.example.academic.system.security;

import org.example.academic.system.exception.AuthorizationException;
import org.example.academic.system.model.Role;
import org.example.academic.system.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes de Autorização")
class AuthorizationServiceTest {

    private AuthorizationService authorizationService;
    private Session session;

    @BeforeEach
    void setup() {
        authorizationService = new AuthorizationService();
        session = Session.getInstance();
        session.logout(); // Limpa a sessão antes de cada teste
    }

    @Test
    @DisplayName("Deve permitir acesso quando usuário tem role ADMIN")
    void testAllowAccessForAdminUser() {
        User adminUser = new User("admin", "admin123", Role.ADMIN);
        session.login(adminUser);

        // Não deve lançar exceção
        assertDoesNotThrow(() -> {
            authorizationService.authorize(Role.ADMIN);
        });
    }

    @Test
    @DisplayName("Deve negar acesso quando usuário não tem role necessária")
    void testDenyAccessForUnauthorizedUser() {
        User professorUser = new User("professor", "prof123", Role.PROFESSOR);
        session.login(professorUser);

        assertThrows(AuthorizationException.class, () -> {
            authorizationService.authorize(Role.ADMIN);
        });
    }

    @Test
    @DisplayName("Deve negar acesso quando nenhum usuário está logado")
    void testDenyAccessWhenNoUserLoggedIn() {
        session.logout();

        assertThrows(AuthorizationException.class, () -> {
            authorizationService.authorize(Role.ADMIN);
        });
    }

    @Test
    @DisplayName("Deve permitir acesso com múltiplos roles (OR lógico)")
    void testAllowAccessWithMultipleRoles() {
        User professorUser = new User("prof", "pass", Role.PROFESSOR);
        session.login(professorUser);

        // Deve permitir se qualquer uma das roles combinar
        assertDoesNotThrow(() -> {
            authorizationService.authorize(Role.ADMIN, Role.PROFESSOR);
        });
    }

    @Test
    @DisplayName("Deve negar acesso se nenhuma role combinar com múltiplas opções")
    void testDenyAccessWhenNoneOfMultipleRolesMatch() {
        User professorUser = new User("prof", "pass", Role.PROFESSOR);
        session.login(professorUser);

        // Role ADMIN não combina, então deve falhar
        assertThrows(AuthorizationException.class, () -> {
            authorizationService.authorize(Role.ADMIN);
        });
    }

    @Test
    @DisplayName("Deve verificar autorização após múltiplos logins")
    void testAuthorizationAfterMultipleLogins() {
        User adminUser = new User("admin", "pass", Role.ADMIN);
        User professorUser = new User("professor", "pass", Role.PROFESSOR);

        session.login(adminUser);
        assertDoesNotThrow(() -> authorizationService.authorize(Role.ADMIN));

        session.login(professorUser);
        assertThrows(AuthorizationException.class, () -> {
            authorizationService.authorize(Role.ADMIN);
        });

        session.login(adminUser);
        assertDoesNotThrow(() -> authorizationService.authorize(Role.ADMIN));
    }

    @Test
    @DisplayName("Deve verificar autorização após logout")
    void testAuthorizationAfterLogout() {
        User adminUser = new User("admin", "pass", Role.ADMIN);
        session.login(adminUser);
        assertDoesNotThrow(() -> authorizationService.authorize(Role.ADMIN));

        session.logout();
        assertThrows(AuthorizationException.class, () -> {
            authorizationService.authorize(Role.ADMIN);
        });
    }

    @Test
    @DisplayName("Deve permitir Professor quando role autorizada é PROFESSOR")
    void testAllowProfessorWhenAuthorizationRequiresProfessor() {
        User professorUser = new User("prof", "pass", Role.PROFESSOR);
        session.login(professorUser);

        assertDoesNotThrow(() -> {
            authorizationService.authorize(Role.PROFESSOR);
        });
    }

    @Test
    @DisplayName("Deve rejeitar se usuário null está logado")
    void testRejectNullUser() {
        session.logout();

        assertThrows(AuthorizationException.class, () -> {
            authorizationService.authorize(Role.ADMIN);
        });
    }

    @Test
    @DisplayName("Deve retornar exceção com mensagem apropriada para falta de permissão")
    void testExceptionMessageForUnauthorized() {
        User professorUser = new User("prof", "pass", Role.PROFESSOR);
        session.login(professorUser);

        AuthorizationException exception = assertThrows(AuthorizationException.class, () -> {
            authorizationService.authorize(Role.ADMIN);
        });

        assertTrue(exception.getMessage().contains("required") || 
                   exception.getMessage().contains("permission") ||
                   exception.getMessage().contains("role") ||
                   exception.getMessage().contains("does not have"));
    }

    @Test
    @DisplayName("Deve retornar exceção com mensagem apropriada para nenhum usuário logado")
    void testExceptionMessageForNoUserLoggedIn() {
        session.logout();

        AuthorizationException exception = assertThrows(AuthorizationException.class, () -> {
            authorizationService.authorize(Role.ADMIN);
        });

        assertTrue(exception.getMessage().contains("authenticated") || 
                   exception.getMessage().contains("user") ||
                   exception.getMessage().contains("No"));
    }
}
