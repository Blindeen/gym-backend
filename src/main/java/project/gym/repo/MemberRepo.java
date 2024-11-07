package project.gym.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import project.gym.enums.Role;
import project.gym.model.Member;

public interface MemberRepo extends CrudRepository<Member, Long>, PagingAndSortingRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    List<Member> findByRoleOrderByIdAsc(Role role);
}
