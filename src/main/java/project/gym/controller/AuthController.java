package project.gym.controller;

import java.time.ZonedDateTime;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import project.gym.Utils;
import project.gym.dto.member.AuthenticationResponse;
import project.gym.dto.member.LoginRequest;
import project.gym.dto.member.RegisterRequest;
import project.gym.dto.member.RegistrationResponse;
import project.gym.service.AuthService;
import project.gym.service.MailService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final MailService mailService;
    private final Utils utils;

    public AuthController(AuthService authService, MailService mailService, Utils utils) {
        this.authService = authService;
        this.mailService = mailService;
        this.utils = utils;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<AuthenticationResponse> signUp(
            HttpServletRequest request,
            @RequestBody @Valid RegisterRequest requestBody) {
        RegistrationResponse registrationResponse = authService.register(requestBody);
        AuthenticationResponse responseBody = registrationResponse.getAuthenticationResponse();

        String email = responseBody.getUser().getEmail();
        String firstName = responseBody.getUser().getFirstName();
        String serverHostName = utils.getServerHostName(request);
        String confirmAccountToken = registrationResponse.getConfirmAccountToken();

        LocaleContextHolder.setLocale(LocaleContextHolder.getLocale(), true);
        mailService.sendSignUpConfirmation(email, firstName, serverHostName, confirmAccountToken);

        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthenticationResponse> signIn(
            HttpServletRequest request,
            @RequestBody @Valid LoginRequest requestBody) {
        AuthenticationResponse responseBody = authService.login(requestBody);

        String emailTo = responseBody.getUser().getEmail();
        String clientIpAddress = utils.getClientAddr(request);
        String browser = utils.getBrowser(request);
        String serverName = utils.getServerHostName(request);
        ZonedDateTime dateTime = utils.nowUTCDateTime();

        LocaleContextHolder.setLocale(LocaleContextHolder.getLocale(), true);
        mailService.sendSignInConfirmation(emailTo, clientIpAddress, browser, serverName, dateTime);

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
