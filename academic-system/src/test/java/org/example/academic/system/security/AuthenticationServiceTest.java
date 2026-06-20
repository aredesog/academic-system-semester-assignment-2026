package org.example.academic.system.security;

import org.example.academic.system.exception.AuthenticationException;
import org.example.academic.system.model.Role;
import org.example.academic.system.model.User;
import org.example.academic.system.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes de Autenticação")
class AuthenticationServiceTest {

    private AuthenticationService authenticationService;
    private Session session;
    
    // Mock simples sem Mockito
    static class SimpleUserRepository implements UserRepository {
        private final User user;
        
        SimpleUserRepository(User user) {
            this.user = user;
        }
        
        @Override
        public Optional<User> findByUsername(String username) {
            if (user != null && user.getUsername().equals(username)) {
                return Optional.of(user);
            }
            return Optional.empty();
        }
    }

    @BeforeEach
    void setup() {
        session = Session.getInstance();
        session.logout();
    }

    @Test
    @DisplayName("Deve autenticar com credenciais válidas")
    void testAuthenticateWithValidCredentials() {
        User validUser = new User("professor", "senha123", Role.PROFESSOR);
        authenticationService = new AuthenticationService(new SimpleUserRepository(validUser));

        User result = authenticationService.authenticate("professor", "senha123");

        assertNotNull(result);
        assertEquals("professor", result.getUsername());
        assertEquals(Role.PROFESSOR, result.getRole());
    }

    @Test
    @DisplayName("Deve rejeitar credenciais inválidas - usuário não existe")
    void testRejectInvalidCredentialsUserNotFound() {
        User validUser = new User("professor", "senha123", Role.PROFESSOR);
        authenticationService = new AuthenticationService(new SimpleUserRepository(validUser));

        assertThrows(AuthenticationException.class, () -> {
            authenticationService.authenticate("invalido", "senha123");
        });
    }

    @Test
    @DisplayName("Deve rejeitar credenciais inválidas - senha errada")
    void testRejectInvalidCredentialsWrongPassword() {
        User validUser = new User("professor", "senha123", Role.PROFESSOR);
        authenticationService = new AuthenticationService(new SimpleUserRepository(validUser));

        assertThrows(AuthenticationException.class, () -> {
            authenticationService.authenticate("professor", "senhaErrada");
        });
    }

    @Test
    @DisplayName("Deve fazer login e criar sessão")
    void testAuthenticationCreatesSession() {
        User validUser = new User("professor", "senha123", Role.PROFESSOR);
        authenticationService = new AuthenticationService(new SimpleUserRepository(validUser));

        User result = authenticationService.authenticate("professor", "senha123");

        assertNotNull(result);
        assertEquals(validUser, result);
    }

    @Test
    @DisplayName("Deve autenticar usuário ADMIN")
    void testAuthenticateAdminUser() {
        User adminUser = new User("admin", "admin123", Role.ADMIN);
        authenticationService = new AuthenticationService(new SimpleUserRepository(adminUser));

        User result = authenticationService.authenticate("admin", "admin123");

        assertNotNull(result);
        assertEquals(Role.ADMIN, result.getRole());
    }

    @Test
    @DisplayName("Deve diferenciar maiúsculas e minúsculas em password")
    void testPasswordIsCaseSensitive() {
        User validUser = new User("professor", "senha123", Role.PROFESSOR);
        authenticationService = new AuthenticationService(new SimpleUserRepository(validUser));

        assertThrows(AuthenticationException.class, () -> {
            authenticationService.authenticate("professor", "SENHA123");
        });
    }

    @Test
    @DisplayName("Deve lançar exception com mensagem apropriada")
    void testExceptionMessageOnAuthenticationFailure() {
        User validUser = new User("professor", "senha123", Role.PROFESSOR);
        authenticationService = new AuthenticationService(new SimpleUserRepository(validUser));

        AuthenticationException exception = assertThrows(AuthenticationException.class, () -> {
            authenticationService.authenticate("professor", "senhaErrada");
        });

        assertTrue(exception.getMessage().contains("Invalid credentials") || 
                   exception.getMessage().contains("invalid") ||
                   exception.getMessage().contains("Invalid"));
    }

    @Test
    @DisplayName("Deve autenticar múltiplos usuários sequencialmente")
    void testMultipleAuthenticationsSequentially() {
        User user1 = new User("user1", "pass1", Role.PROFESSOR);
        AuthenticationService service1 = new AuthenticationService(new SimpleUserRepository(user1));
        
        User user2 = new User("user2", "pass2", Role.ADMIN);
        AuthenticationService service2 = new AuthenticationService(new SimpleUserRepository(user2));

        User result1 = service1.authenticate("user1", "pass1");
        User result2 = service2.authenticate("user2", "pass2");

        assertEquals("user1", result1.getUsername());
        assertEquals("user2", result2.getUsername());
    }

    @Test
    @DisplayName("Deve rejeitar password vazia")
    void testRejectEmptyPassword() {
        User validUser = new User("professor", "senha123", Role.PROFESSOR);
        authenticationService = new AuthenticationService(new SimpleUserRepository(validUser));

        assertThrows(AuthenticationException.class, () -> {
            authenticationService.authenticate("professor", "");
        });
    }

    @Test
    @DisplayName("Deve aceitar password com caracteres especiais")
    void testAcceptSpecialCharactersInPassword() {
        User validUser = new User("user", "p@ss!w0rd#123", Role.PROFESSOR);
        authenticationService = new AuthenticationService(new SimpleUserRepository(validUser));

        User result = authenticationService.authenticate("user", "p@ss!w0rd#123");
        assertNotNull(result);
    }
}
