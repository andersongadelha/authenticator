package br.com.zup.authenticator.services;

import br.com.zup.authenticator.infra.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private static final String CLAIM_DEPARTMENT = "department";
    private static final String CLAIM_SUB = "sub";

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private HttpServletRequest httpServletRequest;
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
}
