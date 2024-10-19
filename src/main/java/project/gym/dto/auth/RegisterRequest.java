package project.gym.dto.auth;

import jakarta.validation.constraints.*;
import lombok.Data;
import project.gym.model.Contact;
import project.gym.model.Member;

import java.time.LocalDate;

@Data
public class RegisterRequest {
    @NotEmpty(message = "{validation.firstNameIsRequired}")
    private String firstName;

    @NotEmpty(message = "{validation.lastNameIsRequired}")
    private String lastName;

    @NotEmpty(message = "{validation.emailIsRequired}")
    @Email(message = "{validation.invalidEmail}")
    private String email;

    @NotEmpty(message = "{validation.passwordIsRequired}")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "{validation.invalidPasswordPattern}"
    )
    private String password;

    @Past(message = "{validation.birthdateInThePast}")
    private LocalDate birthdate;

    @NotEmpty(message = "{validation.addressLineIsRequired}")
    private String addressLine;

    @NotEmpty(message = "{validation.cityIsRequired}")
    private String city;

    @NotEmpty(message = "{validation.postalCodeIsRequired}")
    @Pattern(regexp = "^(\\d{5})|(\\d{5}-\\d{4})|(\\d{2}-\\d{3})$", message = "{validation.invalidPostalCode}")
    private String postalCode;

    @NotEmpty(message = "{validation.phoneNumberIsRequired}")
    @Pattern(regexp = "^(\\+\\d{1,4})((\\(\\d{3}\\)\\d{3}\\-\\d{4})|(\\d{9}))$", message = "{validation.invalidPhoneNumber}")
    private String phoneNumber;

    @NotNull(message = "{validation.paymentMethodIsRequired}")
    private long paymentMethod;

    @NotNull(message = "{validation.passTypeIsRequired}")
    private long passType;

    public Member toMember() {
        return new Member()
                .withFirstName(firstName)
                .withLastName(lastName)
                .withEmail(email)
                .withPassword(password)
                .withBirthdate(birthdate);
    }

    public Contact toContact() {
        return new Contact()
                .withAddressLine(addressLine)
                .withCity(city)
                .withPostalCode(postalCode)
                .withPhoneNumber(phoneNumber);
    }
}
