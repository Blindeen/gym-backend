package project.gym.repo;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import project.gym.model.Activity;
import project.gym.model.Member;

public interface ActivityRepo extends CrudRepository<Activity, Long>, PagingAndSortingRepository<Activity, Long> {
    @NotNull
    Page<Activity> findAll(@NotNull Pageable pageable);

    Page<Activity> findByMembersContains(Member member, Pageable pageable);

    Page<Activity> findByTrainer(Member trainer, Pageable pageable);
}
