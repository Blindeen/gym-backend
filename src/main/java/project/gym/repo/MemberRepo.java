package project.gym.repo;

import org.springframework.data.repository.CrudRepository;
import project.gym.model.Member;

import java.util.Optional;

public interface MemberRepo extends CrudRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
}
