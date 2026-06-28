package org.example.academic.system.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testes da Classe User")
class UserTest {

    private User professorUser;
    private User adminUser;

    @BeforeEach
    void setup() {
        professorUser = new User("professor", "senha123", Role.PROFESSOR);
        adminUser = new User("admin", "admin123", Role.ADMIN);
    }

    @Test
    @DisplayName("Deve criar User com dados válidos")
    void testCreateUserValid() {
        assertNotNull(professorUser);
        assertEquals("professor", professorUser.getUsername());
        assertEquals("senha123", professorUser.getPassword());
        assertEquals(Role.PROFESSOR, professorUser.getRole());
    }

    @Test
    @DisplayName("Deve criar User ADMIN")
    void testCreateAdminUser() {
        assertNotNull(adminUser);
        assertEquals("admin", adminUser.getUsername());
        assertEquals(Role.ADMIN, adminUser.getRole());
    }

    @Test
    @DisplayName("Deve rejeitar username nulo")
    void testRejectNullUsername() {
        assertThrows(NullPointerException.class, () -> {
            new User(null, "senha", Role.PROFESSOR);
        });
    }

    @Test
    @DisplayName("Deve rejeitar password nula")
    void testRejectNullPassword() {
        assertThrows(NullPointerException.class, () -> {
            new User("usuario", null, Role.PROFESSOR);
        });
    }

    @Test
    @DisplayName("Deve rejeitar role nula")
    void testRejectNullRole() {
        assertThrows(NullPointerException.class, () -> {
            new User("usuario", "senha", null);
        });
    }

    @Test
    @DisplayName("Deve considerar iguais dois usuários com mesmo username")
    void testEqualityBasedOnUsername() {
        User other = new User("professor", "outraSenha", Role.ADMIN);
        assertEquals(professorUser, other);
    }

    @Test
    @DisplayName("Deve considerar diferentes dois usuários com username diferentes")
    void testInequalityDifferentUsername() {
        User other = new User("student", "senha123", Role.PROFESSOR);
        assertNotEquals(professorUser, other);
    }

    @Test
    @DisplayName("Deve ter mesmo hashCode para usuários iguais")
    void testHashCodeEqualityForEqualObjects() {
        User other = new User("professor", "outraSenha", Role.ADMIN);
        assertEquals(professorUser.hashCode(), other.hashCode());
    }

    @Test
    @DisplayName("Deve ser igual a si mesmo")
    void testEqualityWithSelf() {
        assertEquals(professorUser, professorUser);
    }

    @Test
    @DisplayName("Não deve ser igual a object de outro tipo")
    void testNotEqualToOtherType() {
        assertNotEquals(professorUser, "professor");
        assertNotEquals(professorUser, 123);
        assertNotEquals(professorUser, null);
    }

    @Test
    @DisplayName("Deve retornar toString formatado")
    void testToStringFormatted() {
        String result = professorUser.toString();
        assertTrue(result.contains("User"));
        assertTrue(result.contains("professor"));
        assertTrue(result.contains("PROFESSOR"));
    }

    @Test
    @DisplayName("Deve imutabilidade de username")
    void testUsernameImmutability() {
        String username = professorUser.getUsername();
        assertEquals("professor", username);
    }

    @Test
    @DisplayName("Deve imutabilidade de password")
    void testPasswordImmutability() {
        String password = professorUser.getPassword();
        assertEquals("senha123", password);
    }

    @Test
    @DisplayName("Deve imutabilidade de role")
    void testRoleImmutability() {
        Role role = professorUser.getRole();
        assertEquals(Role.PROFESSOR, role);
    }

    @Test
    @DisplayName("Deve permitir senhas com espaços e caracteres especiais")
    void testPasswordWithSpecialCharacters() {
        User user = new User("user", "s@nh@123 com espaço", Role.PROFESSOR);
        assertEquals("s@nh@123 com espaço", user.getPassword());
    }

    @Test
    @DisplayName("Deve permitir username com underscores e números")
    void testUsernameWithUnderscoresAndNumbers() {
        User user = new User("prof_2024", "senha", Role.PROFESSOR);
        assertEquals("prof_2024", user.getUsername());
    }
}
