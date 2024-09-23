package project.gym.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import project.gym.dto.form.EditProfileFormData;
import project.gym.dto.form.SignUpFormData;
import project.gym.model.Member;
import project.gym.repo.PassRepo;
import project.gym.repo.PaymentMethodRepo;
import project.gym.service.JwtService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/form")
public class FormController {
    private final PassRepo passRepo;
    private final PaymentMethodRepo paymentMethodRepo;
    private final JwtService jwtService;

    public FormController(PassRepo passRepo, PaymentMethodRepo paymentMethodRepo, JwtService jwtService) {
        this.passRepo = passRepo;
        this.paymentMethodRepo = paymentMethodRepo;
        this.jwtService = jwtService;
    }

    @GetMapping("/sign-up/prepare")
    public SignUpFormData prepareSignUpForm() {
        return new SignUpFormData(passRepo.findAll(), paymentMethodRepo.findAll());
    }

    @GetMapping("/edit-profile/prepare")
    public EditProfileFormData prepareEditProfileForme(@RequestHeader("Authorization") String token) {
        Member member = jwtService.getMember(token);
        return new EditProfileFormData(member, passRepo.findAll(), paymentMethodRepo.findAll());
    }

}
