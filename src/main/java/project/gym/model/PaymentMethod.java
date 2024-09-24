package project.gym.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.*;
import project.gym.enums.PaymentMethodType;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@With
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private PaymentMethodType type;

    @JsonProperty("name")
    public String getName() {
        return type.toString();
    }
}
