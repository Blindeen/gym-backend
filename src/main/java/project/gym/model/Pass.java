package project.gym.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

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
    @JsonIgnore
    private PassType type;

    @Column(nullable = false)
    private float monthlyPrice;

    private Integer length;

    @JsonProperty("name")
    public String getName() {
        return type.toString();
    }
}
