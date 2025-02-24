package br.com.zup.authenticator.controllers.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseDto {
    private String message;
    private String department;
}
