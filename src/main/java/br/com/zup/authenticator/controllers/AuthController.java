package br.com.zup.authenticator.controllers;

import br.com.zup.authenticator.controllers.dtos.AuthResponseDto;
import br.com.zup.authenticator.controllers.dtos.LoginDto;
import br.com.zup.authenticator.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto){
        AuthResponseDto responseDto = authService.login(loginDto);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponseDto> refreshToken(@RequestHeader("refresh-token") String refreshToken){
        AuthResponseDto responseDto = authService.refreshToken(refreshToken);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
