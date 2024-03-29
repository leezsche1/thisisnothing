package inflearn.exercise.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/join", "/join/idCheck", "/login", "/auth/sms", "/auth/smsCheck").permitAll()
                        .anyRequest().authenticated());

        http
                .csrf((auth) -> auth
                        .disable());

        http
                .httpBasic((auth) -> auth
                        .disable());

        http
                .formLogin((auth) -> auth
                        .disable());

        return http.build();
    }

}
