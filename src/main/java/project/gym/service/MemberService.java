package project.gym.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.gym.config.JwtService;
import project.gym.dto.authentication.AuthenticationResponseDto;
import project.gym.dto.authentication.LoginMemberDto;
import project.gym.dto.authentication.RegisterMemberDto;
import project.gym.exception.EmailAlreadyExistException;
import project.gym.model.Contact;
import project.gym.model.Member;
import project.gym.repo.MemberRepo;

@Service
public class MemberService {
    @Autowired
    private MemberRepo memberRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthenticationResponseDto register(RegisterMemberDto request) {
        Member newMember = request.toMember();
        Contact newContact = request.toContact();

        newMember.setPassword(passwordEncoder.encode(newMember.getPassword()));
        newMember.setContact(newContact);

        try {
            memberRepo.save(newMember);
        } catch (DataIntegrityViolationException e) {
            throw new EmailAlreadyExistException();
        }

        String token = jwtService.generateToken(newMember);

        return new AuthenticationResponseDto(newMember, token);
    }

    public AuthenticationResponseDto login(LoginMemberDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        Member member = memberRepo.findByEmail(request.getEmail()).orElseThrow();
        String token = jwtService.generateToken(member);

        return new AuthenticationResponseDto(member, token);
    }
}
