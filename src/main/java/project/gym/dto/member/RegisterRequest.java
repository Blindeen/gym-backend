package project.gym.dto.member;

import jakarta.validation.constraints.*;
import lombok.Data;
import project.gym.model.Contact;
import project.gym.model.Member;

import java.util.Date;

@Data
public class RegisterRequest {
    @NotEmpty(message = "{validation.firstNameIsRequired}")
    protected String firstName;

    @NotEmpty(message = "{validation.lastNameIsRequired}")
    protected String lastName;

    @NotEmpty(message = "{validation.emailIsRequired}")
    @Email(message = "{validation.invalidEmail}")
    protected String email;

    @NotEmpty(message = "{validation.passwordIsRequired}")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "{validation.invalidPasswordPattern}"
    )
    protected String password;

    @Past(message = "{validation.birthdateInThePast}")
    protected Date birthdate;

    @NotEmpty(message = "{validation.addressLineIsRequired}")
    protected String addressLine;

    @NotEmpty(message = "{validation.cityIsRequired}")
    protected String city;

    @NotEmpty(message = "{validation.postalCodeIsRequired}")
    @Pattern(regexp = "^(\\d{5})|(\\d{5}-\\d{4})|(\\d{2}-\\d{3})$", message = "{validation.invalidPostalCode}")
    protected String postalCode;

    @NotEmpty(message = "{validation.phoneNumberIsRequired}")
    @Pattern(regexp = "^(\\+\\d{1,4})((\\(\\d{3}\\)\\d{3}\\-\\d{4})|(\\d{9}))$", message = "{validation.invalidPhoneNumber}")
    protected String phoneNumber;

    @NotNull(message = "{validation.paymentMethodIsRequired}")
    protected long paymentMethod;

    @NotNull(message = "{validation.passTypeIsRequired}")
    protected long passType;

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
