package br.com.zup.authenticator.services;

import br.com.zup.authenticator.controllers.dtos.RegisterUserDto;
import br.com.zup.authenticator.controllers.dtos.Roles;
import br.com.zup.authenticator.controllers.dtos.UserResponseDto;
import br.com.zup.authenticator.models.User;
import br.com.zup.authenticator.repositories.RoleRepository;
import br.com.zup.authenticator.repositories.UserRepository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Test
    public void deveRegistrarUsuarioComSucesso() {
        // Arrange
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setUsername("testUser");
        registerUserDto.setEmail("test@teste.com");
        registerUserDto.setPassword("testPassword");
        registerUserDto.setName("Test Name");
        registerUserDto.setRoles(Set.of(Roles.ROLE_USER));

        when(userRepository.existsByUsername("testUser")).thenReturn(false);
        when(bCryptPasswordEncoder.encode("testPassword")).thenReturn("encodedPassword");

        // Act
        userService.registerUser(registerUserDto);

        // Assert
        verify(userRepository).existsByUsername("testUser");
        verify(bCryptPasswordEncoder).encode("testPassword");
        verify(roleRepository).saveAll(anySet());
        verify(userRepository).save(userArgumentCaptor.capture());
        User userSaved = userArgumentCaptor.getValue();
        assertEquals("encodedPassword",userSaved.getPassword());
    }

    @Test
    public void deveLancarExcecaoQuandoUsuarioJaExiste() {
        // Arrange
        RegisterUserDto registerUserDto = new RegisterUserDto();
        registerUserDto.setUsername("testUser");

        when(userRepository.existsByUsername("testUser")).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.registerUser(registerUserDto);
        });

        assertEquals("Unprocess Entity", exception.getMessage());
        verify(userRepository).existsByUsername("testUser");
    }

    @Test
    public void deveRetornarDetalhesDoUsuarioComSucesso() {
        // Arrange
        String username = "testUser";
        String department = "IT";
        User user = new User();
        user.setUsername(username);
        user.setName("Test Name");

        when(jwtService.getUserName()).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(jwtService.getDepartment()).thenReturn(department);

        // Act
        UserResponseDto response = userService.getDetails();

        // Assert
        assertNotNull(response);
        assertEquals("Bem-vindo, Test Name!", response.getMessage());
        assertEquals(department, response.getDepartment());

        verify(jwtService).getUserName();
        verify(userRepository).findByUsername(username);
        verify(jwtService).getDepartment();
    }

    @Test
    public void deveLancarExcecaoQuandoUsuarioNaoExiste() {
        // Arrange
        String username = "nonExistentUser";

        when(jwtService.getUserName()).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userService.getDetails();
        });

        assertEquals("User not exists by Username or Email", exception.getMessage());
        verify(jwtService).getUserName();
        verify(userRepository).findByUsername(username);
    }
}