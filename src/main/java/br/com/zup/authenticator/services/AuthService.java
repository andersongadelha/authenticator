package br.com.zup.authenticator.services;

import br.com.zup.authenticator.controllers.dtos.AuthResponseDto;
import br.com.zup.authenticator.controllers.dtos.LoginDto;
import br.com.zup.authenticator.infra.jwt.JwtTokenProvider;
import br.com.zup.authenticator.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private JwtService jwtService;

    public AuthResponseDto login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(),
                loginDto.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtTokenProvider.generateToken(authentication);
        String refreshToken = jwtService.createRefreshToken(loginDto.getUsername());

        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthResponseDto refreshToken(String refreshToken) {
        User user = jwtService.verifyExpiration(refreshToken);
        String accessToken = jwtTokenProvider.generateTokenFromUser(user);
        String newRefreshToken = jwtService.createRefreshToken(user.getUsername());

        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken)
                .build();
    }
}
