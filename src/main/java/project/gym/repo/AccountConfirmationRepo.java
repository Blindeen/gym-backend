package project.gym.repo;

import org.springframework.data.repository.CrudRepository;

import project.gym.model.AccountConfirmation;
import java.util.Optional;

public interface AccountConfirmationRepo extends CrudRepository<AccountConfirmation, Long> {
    Optional<AccountConfirmation> findByToken(String token);
}
