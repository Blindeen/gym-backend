package project.gym.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import project.gym.dto.form.SignUpFormData;
import project.gym.repo.PassRepo;
import project.gym.repo.PaymentMethodRepo;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/form")
public class FormController {
    private final PassRepo passRepo;
    private final PaymentMethodRepo paymentMethodRepo;

    public FormController(PassRepo passRepo, PaymentMethodRepo paymentMethodRepo) {
        this.passRepo = passRepo;
        this.paymentMethodRepo = paymentMethodRepo;
    }

    @GetMapping("/sign-up/prepare")
    public SignUpFormData prepareSignUpForm() {
        return new SignUpFormData(passRepo.findAll(), paymentMethodRepo.findAll());
    }
}
