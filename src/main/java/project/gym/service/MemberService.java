package project.gym.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.gym.dto.activity.ActivityResponse;
import project.gym.dto.member.AuthenticationResponse;
import project.gym.dto.member.LoginRequest;
import project.gym.dto.member.RegisterRequest;
import project.gym.dto.member.UpdateMemberRequest;
import project.gym.enums.Role;
import project.gym.exception.EmailAlreadyExistException;
import project.gym.exception.UserDoesNotExistException;
import project.gym.model.Contact;
import project.gym.model.Member;
import project.gym.model.Pass;
import project.gym.model.PaymentMethod;
import project.gym.repo.ActivityRepo;
import project.gym.repo.MemberRepo;
import project.gym.repo.PassRepo;
import project.gym.repo.PaymentMethodRepo;

import java.util.List;

@Service
public class MemberService {
    private final MemberRepo memberRepo;
    private final ActivityRepo activityRepo;
    private final PassRepo passRepo;
    private final PaymentMethodRepo paymentMethodRepo;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public MemberService(
            MemberRepo memberRepo,
            ActivityRepo activityRepo,
            PassRepo passRepo,
            PaymentMethodRepo paymentMethodRepo,
            JwtService jwtService,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager) {
        this.memberRepo = memberRepo;
        this.activityRepo = activityRepo;
        this.passRepo = passRepo;
        this.paymentMethodRepo = paymentMethodRepo;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(RegisterRequest request) {
        Member newMember = request.toMember();
        Contact newContact = request.toContact();

        PaymentMethod paymentMethod = paymentMethodRepo.findPaymentMethodByName(request.getPaymentMethod());
        Pass passType = passRepo.findPassByName(request.getPassType());

        newMember.setPassword(passwordEncoder.encode(newMember.getPassword()));
        newMember.setContact(newContact);
        newMember.setPaymentMethod(paymentMethod);
        newMember.setPass(passType);

        try {
            memberRepo.save(newMember);
        } catch (DataIntegrityViolationException e) {
            throw new EmailAlreadyExistException();
        }

        String token = jwtService.generateToken(newMember);

        return new AuthenticationResponse(newMember, token);
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
