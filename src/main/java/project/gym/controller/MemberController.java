package project.gym.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import project.gym.Utils;
import project.gym.dto.activity.ActivityResponse;
import project.gym.dto.member.AuthenticationResponse;
import project.gym.dto.member.LoginRequest;
import project.gym.dto.member.RegisterRequest;
import project.gym.dto.member.UpdateMemberRequest;
import project.gym.model.Member;
import project.gym.service.JwtService;
import project.gym.service.MailService;
import project.gym.service.MemberService;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final JwtService jwtService;
    private final MailService mailService;
    private final Utils utils;

    public MemberController(MemberService memberService, JwtService jwtService, MailService mailService, Utils utils) {
        this.memberService = memberService;
        this.jwtService = jwtService;
        this.mailService = mailService;
        this.utils = utils;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<AuthenticationResponse> signUp(@RequestBody @Valid RegisterRequest request) {
        AuthenticationResponse responseBody = memberService.register(request);

        String emailTo = responseBody.getUser().getEmail();
        String firstName = responseBody.getUser().getFirstName();

        LocaleContextHolder.setLocale(LocaleContextHolder.getLocale(), true);
        mailService.sendSignUpConfirmation(emailTo, firstName);

        return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthenticationResponse> signIn(
            HttpServletRequest request,
            @RequestBody @Valid LoginRequest requestBody) {

        ZonedDateTime dateTime = utils.getUtcDateTime();
        
        AuthenticationResponse responseBody = memberService.login(requestBody);

        String emailTo = responseBody.getUser().getEmail();
        String browser = utils.getBrowser(request);

        LocaleContextHolder.setLocale(LocaleContextHolder.getLocale(), true);
        mailService.sendSignInConfirmation(emailTo, browser, dateTime);

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
            @RequestBody @Valid UpdateMemberRequest request) {
        Member member = jwtService.getMember(token);
        Member responseBody = memberService.update(member, request);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @GetMapping("/activities")
    public ResponseEntity<Page<ActivityResponse>> getMyActivities(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "") String name) {
        Member member = jwtService.getMember(token);
        Pageable pagination = PageRequest.of(pageNumber - 1, pageSize);
        Page<ActivityResponse> responseBody = memberService.getMyActivities(name, member, pagination);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @GetMapping("/available-activities")
    public ResponseEntity<List<ActivityResponse>> getAvailableActivities(
            @RequestHeader("Authorization") String token) {
        Member member = jwtService.getMember(token);
        List<ActivityResponse> responseBody = memberService.getAvailableActivities(member);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
