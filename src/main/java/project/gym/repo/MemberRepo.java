package project.gym.repo;

import org.springframework.data.repository.CrudRepository;
import project.gym.model.Member;

public interface UserRepo extends CrudRepository<Member, Long> {
}
