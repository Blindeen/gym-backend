package project.gym.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.gym.dto.RegisterMemberDto;
import project.gym.service.MemberService;

@RestController
@RequestMapping("/api/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @PostMapping("/create")
    public void create(@RequestBody @Valid RegisterMemberDto registerMemberDto) {
        memberService.createMember(registerMemberDto);
    }
}
