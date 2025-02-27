package br.com.zup.authenticator.repositories;

import br.com.zup.authenticator.models.User;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void deveEncontrarUsuarioPorUsername() {
        // Arrange
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test@teste.com");
        user.setName("Test Name");
        user.setPassword("testPassword");
        entityManager.persist(user); // Salva diretamente no banco

        // Act
        Optional<User> foundUser = userRepository.findByUsername("testUser");

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals("testUser", foundUser.get().getUsername());
    }

    @Test
    public void deveRetornarVazioQuandoUsuarioNaoExistir() {
        // Act
        Optional<User> foundUser = userRepository.findByUsername("nonExistentUser");

        // Assert
        assertFalse(foundUser.isPresent());
    }

    @Test
    public void deveRetornarTrueSeUsuarioExistirPorUsername() {
        // Arrange
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test@teste.com");
        user.setName("Test Name");
        user.setPassword("testPassword");
        entityManager.persist(user); // Salva diretamente no banco

        // Act
        boolean exists = userRepository.existsByUsername("testUser");

        // Assert
        assertTrue(exists);
    }

    @Test
    public void deveRetornarFalseSeUsuarioNaoExistirPorUsername() {
        // Act
        boolean exists = userRepository.existsByUsername("nonExistentUser");

        // Assert
        assertFalse(exists);
    }
}