package project.gym.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.gym.config.JwtService;
import project.gym.dto.AuthenticationResponse;
import project.gym.dto.LoginMemberDto;
import project.gym.dto.RegisterMemberDto;
import project.gym.exception.EmailAlreadyExistException;
import project.gym.model.Contact;
import project.gym.model.Member;
import project.gym.repo.MemberRepo;


@Service
public class AuthenticationService {
    @Autowired
    private MemberRepo memberRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterMemberDto request) {
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

        return new AuthenticationResponse(token);
    }

    public AuthenticationResponse authentication(LoginMemberDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
                )
        );

        Member member = memberRepo.findByEmail(request.getEmail()).orElseThrow();
        String token = jwtService.generateToken(member);

        return new AuthenticationResponse(token);
    }
}
