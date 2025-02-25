package br.com.zup.authenticator.services;

import br.com.zup.authenticator.infra.jwt.JwtTokenProvider;
import br.com.zup.authenticator.models.RefreshToken;
import br.com.zup.authenticator.models.User;
import br.com.zup.authenticator.repositories.RefreshTokenRepository;
import br.com.zup.authenticator.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class JwtService {
    private static final String CLAIM_DEPARTMENT = "department";
    private static final String CLAIM_SUB = "sub";
    private static final Integer REFRESH_TOKEN_EXPIRATION_MS = 86400000;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    private UserDetailsService userDetailsService;

    public String getCurrentJwt() {
        String bearerToken = httpServletRequest.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

    public String getDepartment() {
        var jwt = getCurrentJwt();

        return jwtTokenProvider.getClaimsFromToken(jwt).get(CLAIM_DEPARTMENT).toString();
    }

    public String getUserName() {
        var jwt = getCurrentJwt();

        return jwtTokenProvider.getClaimsFromToken(jwt).get(CLAIM_SUB).toString();
    }

    public String createRefreshToken(String userName) {
        User user = userRepository.findByUsername(userName) .orElseThrow(() ->
                new UsernameNotFoundException("User not exists by Username or Email"));
        refreshTokenRepository.findByUser(user).ifPresent(existingToken -> {
            refreshTokenRepository.delete(existingToken);
        });
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(REFRESH_TOKEN_EXPIRATION_MS));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);

        return refreshToken.getToken();
    }

    public User verifyExpiration(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Token not exists"));
        if (refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token was expired. Please make a new signin request");
        }
        refreshTokenRepository.delete(refreshToken);

        return refreshToken.getUser();
    }
}
