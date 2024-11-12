package br.com.zup.authenticator.controllers.dtos;

import lombok.Data;

import java.util.Set;

@Data
public class RegisterUserDto {
    private String name;
    private String username;
    private String email;
    private String password;
    private Set<Roles> roles;
}
