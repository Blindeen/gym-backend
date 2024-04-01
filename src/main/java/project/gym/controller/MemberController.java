package project.gym.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.gym.dto.AuthenticationResponse;
import project.gym.dto.LoginMemberDto;
import project.gym.dto.RegisterMemberDto;
import project.gym.service.MemberService;

@RestController
@RequestMapping("/api/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody @Valid RegisterMemberDto request) {
        return ResponseEntity.ok(memberService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid LoginMemberDto request) {
        return ResponseEntity.ok(memberService.authentication(request));
    }
}
