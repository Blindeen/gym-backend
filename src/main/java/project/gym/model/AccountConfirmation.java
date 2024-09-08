package project.gym.model;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

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
public class AccountConfirmation {
    private static final int EXPIRY_TIME = 24 * 60;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    String token;

    Instant generatedAt = Instant.now();

    Instant confirmedAt = null;

    Instant expiresAt = Instant.now().plus(EXPIRY_TIME, ChronoUnit.MINUTES);

    boolean isConfirmed = false;

    @OneToOne
    Member member;
}
