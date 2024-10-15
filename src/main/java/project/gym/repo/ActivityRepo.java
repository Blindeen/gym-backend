package project.gym.repo;

import java.time.DayOfWeek;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import project.gym.model.Activity;
import project.gym.model.Member;
import project.gym.model.Room;

public interface ActivityRepo extends CrudRepository<Activity, Long>, PagingAndSortingRepository<Activity, Long> {
    @NotNull
    Page<Activity> findAll(@NotNull Pageable pageable);

    Page<Activity> findByMembersContainsAndNameContains(Member member, String name, Pageable pageable);

    Page<Activity> findByTrainerAndNameContains(Member trainer, String name, Pageable pageable);

    List<Activity> findByMembersNotContains(Member member);

    List<Activity> findByRoomAndDayOfWeek(Room room, DayOfWeek dayOfWeek);
}
