package project.gym.repo;

import java.time.DayOfWeek;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import project.gym.dto.activities.CustomerActivity;
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

    @Query(value = "SELECT " +
            "a.id, " +
            "a.name, " +
            "a.day_of_week as dayOfWeek, " +
            "a.start_time as startTime, " +
            "a.duration_min as durationMin, " +
            "r.name as roomName, " +
            "concat(t.first_name, ' ', t.last_name) as trainer, " +
            "CASE WHEN am.members_id IS NOT NULL THEN true ELSE false END AS enrolled " +
            "FROM public.activity AS a " +
            "LEFT JOIN public.activity_members AS am ON a.id = am.activities_id AND am.members_id = :memberId " +
            "INNER JOIN public.room as r ON a.room_id = r.id " +
            "INNER JOIN public.member as t ON a.trainer_id = t.id " +
            "WHERE a.name LIKE %:name% " +
            "ORDER BY a.id", nativeQuery = true)
    Page<CustomerActivity> findCustomerActivities(@Param("memberId") Long memberId, @Param("name") String name,
            Pageable pageable);
}
