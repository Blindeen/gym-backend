package project.gym.repo;

import org.springframework.data.repository.CrudRepository;
import project.gym.model.Member;

import java.util.Optional;
import java.util.List;
import project.gym.enums.Role;


public interface MemberRepo extends CrudRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    List<Member> findByRole(Role role);
}
