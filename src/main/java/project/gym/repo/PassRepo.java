package project.gym.repo;

import org.springframework.data.repository.CrudRepository;
import project.gym.enums.PassType;
import project.gym.model.Pass;

public interface PassRepo extends CrudRepository<Pass, Long> {
    Pass findPassByType(PassType type);
}
