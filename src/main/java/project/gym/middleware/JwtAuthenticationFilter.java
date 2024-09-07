package project.gym.middleware;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import project.gym.service.JwtService;
import project.gym.service.UserDetailsImplService;

import java.util.Arrays;
import java.util.Locale;

import static project.gym.Endpoints.*;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Value("${server.servlet.context-path}")
    private String BASE_PATH;

    private final JwtService jwtService;
    private final UserDetailsImplService userDetailsImplService;
    @Qualifier("handlerExceptionResolver")
    private final HandlerExceptionResolver resolver;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserDetailsImplService userDetailsImplService,
            @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.jwtService = jwtService;
        this.userDetailsImplService = userDetailsImplService;
        this.resolver = resolver;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) {
        Locale locale = LocaleContextHolder.getLocale();
        request.setAttribute("locale", locale);

        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer")) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = authHeader.substring(7);
            String username = jwtService.extractUsername(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsImplService.loadUserByUsername(username);

                if (jwtService.isValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }

                filterChain.doFilter(request, response);
            }
        } catch (Exception e) {
            resolver.resolveException(request, response, null, e);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String[] ignoredPaths = {
                BASE_PATH + SIGN_IN,
                BASE_PATH + SIGN_UP,
                BASE_PATH + LIST_ACTIVITIES,
                BASE_PATH + PREPARE_SIGN_UP_FORM,
                BASE_PATH + CONFIRM_ACCOUNT
        };
        String path = request.getRequestURI();
        return Arrays.asList(ignoredPaths).contains(path);
    }
}
