package project.gym.service;

import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import project.gym.Utils;
import project.gym.dto.auth.AuthenticationResponse;
import project.gym.dto.auth.LoginRequest;
import project.gym.dto.auth.RefreshTokenResponse;
import project.gym.dto.auth.RegisterRequest;
import project.gym.dto.auth.RegistrationResponse;
import project.gym.exception.UserDoesNotExistException;
import project.gym.exception.auth.EmailAlreadyExistException;
import project.gym.exception.auth.PassTypeDoesNotExistException;
import project.gym.exception.auth.PaymentMethodDoesNotExist;
import project.gym.model.AccountConfirmation;
import project.gym.model.Contact;
import project.gym.model.Member;
import project.gym.model.Pass;
import project.gym.model.PassType;
import project.gym.model.PaymentMethod;
import project.gym.repo.AccountConfirmationRepo;
import project.gym.repo.MemberRepo;
import project.gym.repo.PassTypeRepo;
import project.gym.repo.PaymentMethodRepo;

@Service
public class AuthService {
    private final MemberRepo memberRepo;
    private final PaymentMethodRepo paymentMethodRepo;
    private final PassTypeRepo passTypeRepo;
    private final AccountConfirmationRepo accountConfirmationRepo;

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final Utils utils;

    public AuthService(
            MemberRepo memberRepo, PaymentMethodRepo paymentMethodRepo, PassTypeRepo passTypeRepo,
            AccountConfirmationRepo accountConfirmationRepo,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            Utils utils) {
        this.memberRepo = memberRepo;
        this.paymentMethodRepo = paymentMethodRepo;
        this.passTypeRepo = passTypeRepo;
        this.accountConfirmationRepo = accountConfirmationRepo;

        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.utils = utils;
    }

    public RegistrationResponse register(RegisterRequest request) {
        Member newMember = request.toMember();
        Contact newContact = request.toContact();
        PaymentMethod paymentMethod = paymentMethodRepo.findById(request.getPaymentMethod())
                .orElseThrow(PaymentMethodDoesNotExist::new);
        PassType passType = passTypeRepo.findById(request.getPassType())
                .orElseThrow(PassTypeDoesNotExistException::new);

        String accountConfirmationToken = utils.generateUniqueToken();
        AccountConfirmation accountConfirmation = new AccountConfirmation().withToken(accountConfirmationToken);

        newMember.setPassword(passwordEncoder.encode(newMember.getPassword()));
        newMember.setContact(newContact);
        newMember.setPaymentMethod(paymentMethod);
        Pass pass = new Pass().withMember(newMember).withType(passType).withUuid(UUID.randomUUID().toString());
        newMember.setPass(pass);
        newMember.setAccountConfirmation(accountConfirmation);

        try {
            newMember = memberRepo.save(newMember);
        } catch (DataIntegrityViolationException e) {
            throw new EmailAlreadyExistException();
        }

        accountConfirmation = newMember.getAccountConfirmation().withMember(newMember);
        accountConfirmationRepo.save(accountConfirmation);

        String accessToken = jwtService.generateAccessToken(newMember);
        String refreshToken = jwtService.generateRefreshToken(newMember);

        return new RegistrationResponse(new AuthenticationResponse(newMember, accessToken, refreshToken),
                accountConfirmationToken);
    }

    public AuthenticationResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        Member member = memberRepo.findByEmail(request.getEmail()).orElseThrow(UserDoesNotExistException::new);
        String accessToken = jwtService.generateAccessToken(member);
        String refreshToken = jwtService.generateRefreshToken(member);

        return new AuthenticationResponse(member, accessToken, refreshToken);
    }

    public RefreshTokenResponse refreshToken(String token) {
        Member member = jwtService.getMember(token);

        String accessToken = jwtService.generateAccessToken(member);
        String refreshToken = jwtService.generateRefreshToken(member);

        return new RefreshTokenResponse(accessToken, refreshToken);
    }
}
