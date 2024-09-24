package project.gym.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import project.gym.model.Contact;

@Data
public class UpdateMemberRequest {
    @NotEmpty(message = "{validation.firstNameIsRequired}")
    private String firstName;

    @NotEmpty(message = "{validation.lastNameIsRequired}")
    private String lastName;

    @NotEmpty(message = "{validation.emailIsRequired}")
    @Email(message = "{validation.invalidEmail}")
    private String email;

    @Pattern(regexp = "^()|(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "{validation.invalidPasswordPattern}")
    private String password;

    @Pattern(regexp = "^()|(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "{validation.invalidPasswordPattern}")
    private String newPassword;

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

    public Contact toContact() {
        return new Contact()
                .withAddressLine(addressLine)
                .withCity(city)
                .withPostalCode(postalCode)
                .withPhoneNumber(phoneNumber);
    }
}
