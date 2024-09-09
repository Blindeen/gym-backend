package project.gym.model;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@With
public class PasswordReset {
    public static final int EXPIRY_TIME = 24 * 60;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    String token;

    Instant lastAttemptAt;

    Instant expiresAt;

    @OneToOne
    Member member;
}
