package project.gym.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import project.gym.dto.form.AddEditActivityFormData;
import project.gym.dto.form.EditProfileFormData;
import project.gym.dto.form.SignUpFormData;
import project.gym.model.Member;
import project.gym.repo.PassTypeRepo;
import project.gym.repo.PaymentMethodRepo;
import project.gym.repo.RoomRepo;
import project.gym.service.JwtService;

@RestController
@RequestMapping("/form")
public class FormController {
    private final PassTypeRepo passTypeRepo;
    private final PaymentMethodRepo paymentMethodRepo;
    private final RoomRepo roomRepo;
    private final JwtService jwtService;

    public FormController(PassTypeRepo passTypeRepo, PaymentMethodRepo paymentMethodRepo, RoomRepo roomRepo,
            JwtService jwtService) {
        this.passTypeRepo = passTypeRepo;
        this.paymentMethodRepo = paymentMethodRepo;
        this.roomRepo = roomRepo;
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

    @GetMapping("/add-edit-activity/prepare")
    public ResponseEntity<AddEditActivityFormData> prepareAddEditActivityForm() {
        return new ResponseEntity<>(new AddEditActivityFormData(roomRepo.findAll()), HttpStatus.OK);
    }

}
