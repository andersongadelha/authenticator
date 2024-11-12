package br.com.zup.authenticator.controllers;

import br.com.zup.authenticator.controllers.dtos.RegisterUserDto;
import br.com.zup.authenticator.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public void registerUser(@RequestBody RegisterUserDto registerUserDto){
        userService.registerUser(registerUserDto);
    }
}
