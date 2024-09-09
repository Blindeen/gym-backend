package project.gym.repo;

import org.springframework.data.repository.CrudRepository;

import project.gym.model.PasswordReset;

public interface PasswordResetRepo extends CrudRepository<PasswordReset, Long> {

}
