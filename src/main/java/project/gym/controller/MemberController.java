package project.gym.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import project.gym.dto.activities.ActivityResponse;
import project.gym.dto.activities.CustomerActivityResponse;
import project.gym.dto.cloudinary.UploadImageResponse;
import project.gym.dto.members.ConfirmAccountRequest;
import project.gym.dto.members.UpdateMemberRequest;
import project.gym.dto.members.pass.PassBasics;
import project.gym.dto.members.password.ChangePasswordRequest;
import project.gym.dto.members.password.ResetPasswordRequest;
import project.gym.dto.members.trainers.TrainerInfo;
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

        if (passwordReset != null) {
            String email = requestBody.getEmail();
            String token = passwordReset.getToken();
            String serverName = utils.getServerHostName(request);
            mailService.sendPasswordReset(email, token, serverName);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/password/change")
    public ResponseEntity<Void> changePassword(@RequestBody @Valid ChangePasswordRequest requestBody) {
        memberService.changePassword(requestBody);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/profile")
    public ResponseEntity<Member> updateProfile(
            @RequestHeader("Authorization") String token,
            @RequestBody @Valid UpdateMemberRequest requestBody) {
        Member member = jwtService.getMember(token);
        Member responseBody = memberService.update(member, requestBody);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PutMapping(path = "/avatar", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<UploadImageResponse> uploadAvatar(
            @RequestHeader("Authorization") String token,
            @RequestPart MultipartFile image) {
        Member member = jwtService.getMember(token);
        UploadImageResponse responseBody = memberService.uploadAvatar(member, image);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @GetMapping("/pass")
    public ResponseEntity<PassBasics> getPass(@RequestHeader("Authorization") String token) {
        Member member = jwtService.getMember(token);
        PassBasics responseBody = memberService.getPassBasics(member);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @GetMapping("/trainers/activities")
    public ResponseEntity<Page<ActivityResponse>> getTrainerActivities(@RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "") String name) {
        Member member = jwtService.getMember(token);
        Pageable pagination = PageRequest.of(pageNumber - 1, pageSize);
        Page<ActivityResponse> responseBody = memberService.getTrainerActivities(member, name, pagination);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @GetMapping("/customers/activities")
    public ResponseEntity<Page<CustomerActivityResponse>> getCustomerActivities(@RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "") String name) {
        Member member = jwtService.getMember(token);
        Pageable pagination = PageRequest.of(pageNumber - 1, pageSize);
        Page<CustomerActivityResponse> responseBody = memberService.getCustomerActivities(member, name, pagination);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @GetMapping("/trainers")
    public ResponseEntity<List<TrainerInfo>> getTrainers() {
        List<TrainerInfo> responseBody = memberService.getTrainers();
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
