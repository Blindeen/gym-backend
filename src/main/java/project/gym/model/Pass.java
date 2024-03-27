package project.gym.model;

import jakarta.persistence.*;
import lombok.*;
import project.gym.enums.PassType;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@With
public class Pass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PassType type;

    @Column(nullable = false)
    private float monthlyPrice;

    private int length;
}
