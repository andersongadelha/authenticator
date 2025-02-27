package configuracao;

import br.com.zup.authenticator.models.Role;
import br.com.zup.authenticator.models.User;
import br.com.zup.authenticator.repositories.RoleRepository;
import br.com.zup.authenticator.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest
@Transactional
public abstract class BaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {

        if (!userRepository.existsByUsername("testUser")) {
            User user = new User();
            Set<Role> roles = new HashSet<>();
            roles.add(new Role("ROLE_ADMIN"));
            user.setEmail("teste@teste.com");
            user.setName("Nome teste");
            user.setUsername("testUser");
            user.setRoles(roles);
            user.setPassword(passwordEncoder.encode("testPassword"));

            userRepository.save(user);
        }
    }
}