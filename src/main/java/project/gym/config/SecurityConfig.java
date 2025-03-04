package project.gym.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import project.gym.enums.Role;
import project.gym.middleware.JwtAuthenticationFilter;
import project.gym.service.UserDetailsImplService;

import static project.gym.Endpoints.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
        private final UserDetailsImplService userDetailsImplService;
        private final JwtAuthenticationFilter jwtAuthenticationFilter;
        private final AuthenticationEntryPoint authEntryPoint;

        public SecurityConfig(
                        UserDetailsImplService userDetailsImplService,
                        JwtAuthenticationFilter jwtAuthenticationFilter,
                        AuthenticationEntryPoint authEntryPoint) {
                this.userDetailsImplService = userDetailsImplService;
                this.jwtAuthenticationFilter = jwtAuthenticationFilter;
                this.authEntryPoint = authEntryPoint;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                return http
                                .csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(
                                                req -> req
                                                                .requestMatchers(HttpMethod.POST, CREATE_ACTIVITY)
                                                                .hasRole(String.valueOf(Role.TRAINER))
                                                                .requestMatchers(
                                                                                UPDATE_ACTIVITY,
                                                                                DELETE_ACTIVITY,
                                                                                PREPARE_ADD_EDIT_ACTIVITY_FORM,
                                                                                GET_TRAINER_ACTIVITIES)
                                                                .hasRole(String.valueOf(Role.TRAINER))
                                                                .requestMatchers(ENROLL_ACTIVITY, LEAVE_ACTIVITY,
                                                                                GET_CUSTOMER_ACTIVITIES)
                                                                .hasRole(String.valueOf(Role.CUSTOMER))
                                                                .requestMatchers(UPDATE_MEMBER,
                                                                                PREPARE_EDIT_PROFILE_FORM)
                                                                .authenticated()
                                                                .anyRequest()
                                                                .permitAll())
                                .userDetailsService(userDetailsImplService)
                                .exceptionHandling(e -> e.accessDeniedHandler(
                                                (request, response, accessDeniedException) -> response.setStatus(403))
                                                .authenticationEntryPoint(authEntryPoint))
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
