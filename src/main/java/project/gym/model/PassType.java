package project.gym.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;
import project.gym.enums.PassTypeEnum;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@With
public class PassType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private PassTypeEnum type;

    @Column(nullable = false)
    private float monthlyPrice;

    private Integer length;

    @JsonProperty("name")
    public String getName() {
        return type.toString();
    }
}
