package project.gym.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import project.gym.dto.forms.AddEditActivityFormData;
import project.gym.dto.forms.EditProfileFormData;
import project.gym.dto.forms.SignUpFormData;
import project.gym.model.Member;
import project.gym.repo.PassTypeRepo;
import project.gym.repo.PaymentMethodRepo;
import project.gym.repo.RoomRepo;
import project.gym.service.JwtService;

@RestController
@RequestMapping("/forms")
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

    @GetMapping("/sign-up")
    public ResponseEntity<SignUpFormData> prepareSignUpForm() {
        SignUpFormData signUpFormData = new SignUpFormData(passTypeRepo.findAll(), paymentMethodRepo.findAll());
        return new ResponseEntity<>(signUpFormData, HttpStatus.OK);
    }

    @GetMapping("/edit-profile")
    public ResponseEntity<EditProfileFormData> prepareEditProfileForme(@RequestHeader("Authorization") String token) {
        Member member = jwtService.getMember(token);
        EditProfileFormData editProfileFormData = new EditProfileFormData(member);
        return new ResponseEntity<>(editProfileFormData, HttpStatus.OK);
    }

    @GetMapping("/add-edit-activity")
    public ResponseEntity<AddEditActivityFormData> prepareAddEditActivityForm() {
        AddEditActivityFormData addEditActivityFormData = new AddEditActivityFormData(roomRepo.findAll());
        return new ResponseEntity<>(addEditActivityFormData, HttpStatus.OK);
    }
}
