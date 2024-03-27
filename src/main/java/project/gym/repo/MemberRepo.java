package project.gym.repo;

import org.springframework.data.repository.CrudRepository;
import project.gym.model.Member;

public interface MemberRepo extends CrudRepository<Member, Long> {
}
