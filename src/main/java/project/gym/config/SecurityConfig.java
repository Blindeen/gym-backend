package project.gym.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import project.gym.enums.Role;
import project.gym.service.UserDetailsImplService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserDetailsImplService userDetailsImplService;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(UserDetailsImplService userDetailsImplService, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userDetailsImplService = userDetailsImplService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        req -> req.requestMatchers("/api/member/login", "/api/member/register")
                                .permitAll()
                                .requestMatchers(
                                        "/api/activity/create",
                                        "/api/activity/{id}/update",
                                        "/api/activity/{id}/delete"
                                )
                                .hasRole(String.valueOf(Role.TRAINER))
                                .requestMatchers("/api/activity/{id}/enroll")
                                .hasRole(String.valueOf(Role.CUSTOMER))
                                .anyRequest()
                                .authenticated()
                ).userDetailsService(userDetailsImplService)
                .exceptionHandling(e -> e.accessDeniedHandler(
                                (request, response, accessDeniedException) -> response.setStatus(403)
                        )
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
