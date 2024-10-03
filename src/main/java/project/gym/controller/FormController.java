package project.gym.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import project.gym.dto.form.EditProfileFormData;
import project.gym.dto.form.SignUpFormData;
import project.gym.model.Member;
import project.gym.repo.PassTypeRepo;
import project.gym.repo.PaymentMethodRepo;
import project.gym.service.JwtService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/form")
public class FormController {
    private final PassTypeRepo passTypeRepo;
    private final PaymentMethodRepo paymentMethodRepo;
    private final JwtService jwtService;

    public FormController(PassTypeRepo passTypeRepo, PaymentMethodRepo paymentMethodRepo, JwtService jwtService) {
        this.passTypeRepo = passTypeRepo;
        this.paymentMethodRepo = paymentMethodRepo;
        this.jwtService = jwtService;
    }

    @GetMapping("/sign-up/prepare")
    public SignUpFormData prepareSignUpForm() {
        return new SignUpFormData(passTypeRepo.findAll(), paymentMethodRepo.findAll());
    }

    @GetMapping("/edit-profile/prepare")
    public EditProfileFormData prepareEditProfileForme(@RequestHeader("Authorization") String token) {
        Member member = jwtService.getMember(token);
        return new EditProfileFormData(member);
    }
}
