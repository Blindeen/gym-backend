package project.gym.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import project.gym.model.Contact;
import project.gym.model.Member;

import java.util.Date;

@Data
public class RegisterUserDto {
    @Size(min = 2, message = "First name must be at least 2 characters long")

    @NotEmpty(message = "First name is required")
    private String firstName;

    @NotEmpty(message = "Last name is required")
    private String lastName;

    @NotEmpty(message = "Email is required")
    private String email;

    @NotEmpty(message = "Password is required")
    private String password;

    private Date birthdate;

    private String addressLine;

    private String city;

    private String postalCode;

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
