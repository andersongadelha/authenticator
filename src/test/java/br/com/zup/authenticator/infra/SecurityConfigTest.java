package br.com.zup.authenticator.infra;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;

class SecurityConfigTest {
    @Test
    void passwordEncoderBeanShouldBeBCryptPasswordEncoder() {
        SecurityConfig securityConfig = new SecurityConfig(null, null, null);
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();

        assertThat(passwordEncoder).isNotNull();
        assertThat(passwordEncoder).isInstanceOf(org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.class);
    }

    @Test
    void authenticationManagerBeanShouldBeConfigured() throws Exception {
        AuthenticationConfiguration configuration = mock(AuthenticationConfiguration.class);
        SecurityConfig securityConfig = new SecurityConfig(null, null, null);

        AuthenticationManager authenticationManager = securityConfig.authenticationManager(configuration);

        assertThat(authenticationManager).isNotNull();
    }

}