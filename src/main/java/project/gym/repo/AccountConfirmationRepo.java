package project.gym.repo;

import org.springframework.data.repository.CrudRepository;

import project.gym.model.AccountConfirmation;

public interface AccountConfirmationRepo extends CrudRepository<AccountConfirmation, Long> {

}
