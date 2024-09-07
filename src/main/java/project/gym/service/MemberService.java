package project.gym.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import project.gym.Utils;
import project.gym.dto.activity.ActivityResponse;
import project.gym.dto.member.AuthenticationResponse;
import project.gym.dto.member.ConfirmAccountRequest;
import project.gym.dto.member.LoginRequest;
import project.gym.dto.member.RegisterRequest;
import project.gym.dto.member.RegistrationResponse;
import project.gym.dto.member.UpdateMemberRequest;
import project.gym.enums.Role;
import project.gym.exception.AccountAlreadyConfirmed;
import project.gym.exception.EmailAlreadyExistException;
import project.gym.exception.InvalidTokenException;
import project.gym.exception.PassTypeDoesNotExistException;
import project.gym.exception.PaymentMethodDoesNotExist;
import project.gym.exception.UserDoesNotExistException;
import project.gym.model.AccountConfirmation;
import project.gym.model.Contact;
import project.gym.model.Member;
import project.gym.model.Pass;
import project.gym.model.PaymentMethod;
import project.gym.repo.AccountConfirmationRepo;
import project.gym.repo.ActivityRepo;
import project.gym.repo.MemberRepo;
import project.gym.repo.PassRepo;
import project.gym.repo.PaymentMethodRepo;

import java.util.List;
import java.time.Instant;

@Service
public class MemberService {
    private final MemberRepo memberRepo;
    private final ActivityRepo activityRepo;
    private final PassRepo passRepo;
    private final PaymentMethodRepo paymentMethodRepo;
    private final AccountConfirmationRepo accountConfirmationRepo;

    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final Utils utils;

    public MemberService(
            MemberRepo memberRepo,
            ActivityRepo activityRepo,
            PassRepo passRepo,
            PaymentMethodRepo paymentMethodRepo,
            AccountConfirmationRepo accountConfirmationRepo,
            JwtService jwtService,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            Utils utils) {
        this.memberRepo = memberRepo;
        this.activityRepo = activityRepo;
        this.passRepo = passRepo;
        this.paymentMethodRepo = paymentMethodRepo;
        this.accountConfirmationRepo = accountConfirmationRepo;

        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.utils = utils;
    }

    public RegistrationResponse register(RegisterRequest request) {
        Member newMember = request.toMember();
        Contact newContact = request.toContact();
        PaymentMethod paymentMethod = paymentMethodRepo.findById(request.getPaymentMethod())
                .orElseThrow(PaymentMethodDoesNotExist::new);
        Pass passType = passRepo.findById(request.getPassType()).orElseThrow(PassTypeDoesNotExistException::new);

        String accountConfirmationToken = utils.generateUniqueToken();
        AccountConfirmation accountConfirmation = new AccountConfirmation().withToken(accountConfirmationToken);

        newMember.setPassword(passwordEncoder.encode(newMember.getPassword()));
        newMember.setContact(newContact);
        newMember.setPaymentMethod(paymentMethod);
        newMember.setPass(passType);
        newMember.setAccountConfirmation(accountConfirmation);

        try {
            newMember = memberRepo.save(newMember);
        } catch (DataIntegrityViolationException e) {
            throw new EmailAlreadyExistException();
        }

        accountConfirmation = newMember.getAccountConfirmation().withMember(newMember);
        accountConfirmationRepo.save(accountConfirmation);

        String token = jwtService.generateToken(newMember);

        return new RegistrationResponse(new AuthenticationResponse(newMember, token), accountConfirmationToken);
    }

    public AuthenticationResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        Member member = memberRepo.findByEmail(request.getEmail()).orElseThrow(UserDoesNotExistException::new);
        String token = jwtService.generateToken(member);

        return new AuthenticationResponse(member, token);
    }

    public void confirmAccount(ConfirmAccountRequest request) {
        String confirmAccountToken = request.getToken();
        AccountConfirmation accountConfirmation = accountConfirmationRepo.findByToken(confirmAccountToken)
                .orElseThrow(InvalidTokenException::new);
        if (accountConfirmation.isConfirmed()) {
            throw new AccountAlreadyConfirmed();
        }
        accountConfirmation.setConfirmed(true);
        accountConfirmation.setConfirmedAt(Instant.now());
        accountConfirmationRepo.save(accountConfirmation);
    }

    public Member update(Member member, UpdateMemberRequest request) {
        member.setFirstName(request.getFirstName());
        member.setLastName(request.getLastName());
        member.setEmail(request.getEmail());
        member.setBirthdate(request.getBirthdate());
        member.setContact(request.toContact());

        String newPassword = request.getNewPassword();
        if (newPassword != null) {
            String currentPassword = request.getPassword();
            boolean currentMatchesOld = passwordEncoder.matches(currentPassword, member.getPassword());
            if (!currentMatchesOld) {
                throw new BadCredentialsException("Incorrect old password");
            }

            member.setPassword(passwordEncoder.encode(newPassword));
        }

        try {
            return memberRepo.save(member);
        } catch (DataIntegrityViolationException e) {
            throw new EmailAlreadyExistException();
        }
    }

    public Page<ActivityResponse> getMyActivities(String name, Member member, Pageable pagination) {
        Role role = member.getRole();
        if (role == Role.Trainer) {
            return activityRepo.findByTrainerAndNameContains(member, name, pagination)
                    .map(ActivityResponse::valueOf);
        } else {
            return activityRepo.findByMembersContainsAndNameContains(member, name, pagination)
                    .map(ActivityResponse::valueOf);
        }
    }

    public List<ActivityResponse> getAvailableActivities(Member member) {
        return activityRepo.findByMembersNotContains(member)
                .stream().map(ActivityResponse::valueOf).toList();
    }
}
