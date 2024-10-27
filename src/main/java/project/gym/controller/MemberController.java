package project.gym.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import project.gym.Utils;
import project.gym.dto.activity.ActivityResponse;
import project.gym.dto.member.ChangePasswordRequest;
import project.gym.dto.member.ConfirmAccountRequest;
import project.gym.dto.member.ResetPasswordRequest;
import project.gym.dto.member.UpdateMemberRequest;
import project.gym.dto.pass.PassBasics;
import project.gym.model.Member;
import project.gym.model.PasswordReset;
import project.gym.service.JwtService;
import project.gym.service.MailService;
import project.gym.service.MemberService;

@RestController
@RequestMapping("/members")
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

    @PutMapping("/confirmation")
    public ResponseEntity<Void> confirmAccount(@RequestBody @Valid ConfirmAccountRequest requestBody) {
        memberService.confirmAccount(requestBody);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/password/reset")
    public ResponseEntity<Void> resetPassword(
            HttpServletRequest request,
            @RequestBody @Valid ResetPasswordRequest requestBody) {
        PasswordReset passwordReset = memberService.resetPassword(requestBody);

        String email = requestBody.getEmail();
        String token = passwordReset.getToken();
        String serverName = utils.getServerHostName(request);
        mailService.sendPasswordReset(email, token, serverName);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/password/change")
    public ResponseEntity<Void> changePassword(@RequestBody @Valid ChangePasswordRequest requestBody) {
        memberService.changePassword(requestBody);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(path = "/profile", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Member> updateProfile(
            @RequestHeader("Authorization") String token,
            @RequestPart(required = false) MultipartFile profilePicture,
            @RequestPart @Valid UpdateMemberRequest requestBody) {
        Member member = jwtService.getMember(token);
        Member responseBody = memberService.update(member, requestBody, profilePicture);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @GetMapping("/pass")
    public ResponseEntity<PassBasics> getPass(@RequestHeader("Authorization") String token) {
        Member member = jwtService.getMember(token);
        PassBasics responseBody = memberService.getPassBasics(member);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @GetMapping("/activities")
    public ResponseEntity<Page<ActivityResponse>> getActivities(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "") String name) {
        Member member = jwtService.getMember(token);
        Pageable pagination = PageRequest.of(pageNumber - 1, pageSize, Sort.by("id"));
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
