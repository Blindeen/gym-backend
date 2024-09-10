package project.gym.repo;

import org.springframework.data.repository.CrudRepository;

import project.gym.model.PasswordReset;
import java.util.Optional;

public interface PasswordResetRepo extends CrudRepository<PasswordReset, Long> {
    Optional<PasswordReset> findByToken(String token);
}
