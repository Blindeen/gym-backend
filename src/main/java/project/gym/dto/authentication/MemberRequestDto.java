package project.gym.dto.authentication;

import jakarta.validation.constraints.*;
import lombok.Data;
import project.gym.model.Contact;
import project.gym.model.Member;

import java.util.Date;

@Data
public class MemberRequestDto {
    @NotEmpty(message = "First name is required")
    private String firstName;

    @NotEmpty(message = "Last name is required")
    private String lastName;

    @NotEmpty(message = "Email is required")
    @Email(message = "Invalid email")
    private String email;

    @NotEmpty(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must contain at least 8 characters, one uppercase letter, one lowercase letter, one number and one special character"
    )
    private String password;

    @Past(message = "Birthdate must be in the past")
    private Date birthdate;

    @NotEmpty(message = "Address line is required")
    private String addressLine;

    @NotEmpty(message = "City is required")
    private String city;

    @NotEmpty(message = "Postal code is required")
    @Pattern(regexp = "^\\d{5}([ \\-]\\d{4})?$", message = "Invalid postal code")
    private String postalCode;

    @NotEmpty(message = "Phone number is required")
    @Pattern(regexp = "^(\\+\\d{1,3})(\\d{9})$", message = "Invalid phone number")
    private String phoneNumber;

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
