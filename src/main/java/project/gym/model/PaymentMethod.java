package project.gym.model;

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
    private PaymentMethodType type;
}
