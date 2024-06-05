package project.gym.repo;

import org.springframework.data.repository.CrudRepository;
import project.gym.model.Pass;

public interface PassRepo extends CrudRepository<Pass, Long> {
}
