package br.com.zup.authenticator.services;

import br.com.zup.authenticator.controllers.dtos.RegisterUserDto;
import br.com.zup.authenticator.controllers.dtos.UserResponseDto;
import br.com.zup.authenticator.models.Role;
import br.com.zup.authenticator.models.User;
import br.com.zup.authenticator.repositories.RoleRepository;
import br.com.zup.authenticator.repositories.UserRepository;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void registerUser(RegisterUserDto registerUserDto){
        if (userRepository.existsByUsername(registerUserDto.getUsername())){
            throw new RuntimeException("Unprocess Entity");
        }

        User user = new User();
        user.setUsername(registerUserDto.getUsername());
        user.setEmail(registerUserDto.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(registerUserDto.getPassword()));
        user.setName(registerUserDto.getName());

        Set<Role> roles = registerUserDto.getRoles().stream().map(r -> new Role(r.name())).collect(Collectors.toSet());
        roleRepository.saveAll(roles);

        user.setRoles(roles);
        userRepository.save(user);

    }

    public UserResponseDto getDetails() {
        String userName = jwtService.getUserName();
        User user = userRepository.findByUsername(userName) .orElseThrow(() ->
                new UsernameNotFoundException("User not exists by Username or Email"));

        String message = String.format("Bem-vindo, %s!", user.getName());
        String department = jwtService.getDepartment();

        return new UserResponseDto(message, department);
    }
}
