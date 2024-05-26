package project.gym.repo;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import project.gym.model.Activity;

public interface ActivityRepo extends CrudRepository<Activity, Long>, PagingAndSortingRepository<Activity, Long> {
    @NotNull
    Page<Activity> findAll(@NotNull Pageable pageable);
}
