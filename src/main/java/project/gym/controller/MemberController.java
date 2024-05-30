package project.gym.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.gym.config.JwtService;
import project.gym.dto.authentication.AuthenticationResponseDto;
import project.gym.dto.authentication.LoginMemberDto;
import project.gym.dto.authentication.MemberRequestDto;
import project.gym.dto.authentication.UpdateMemberRequest;
import project.gym.model.Member;
import project.gym.service.MemberService;

@RestController
@RequestMapping("/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDto> register(@RequestBody @Valid MemberRequestDto request) {
        AuthenticationResponseDto responseBody = memberService.register(request);
        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> login(@RequestBody @Valid LoginMemberDto request) {
        AuthenticationResponseDto responseBody = memberService.login(request);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<Member> getInfo(@RequestHeader("Authorization") String token) {
        Member responseBody = jwtService.getMember(token);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Member> update(
            @RequestHeader("Authorization") String token,
            @RequestBody @Valid UpdateMemberRequest request
    ) {
        Member member = jwtService.getMember(token);
        Member responseBody = memberService.update(member, request);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
