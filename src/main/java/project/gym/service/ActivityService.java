package project.gym.service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import project.gym.dto.activities.ActivityResponse;
import project.gym.dto.activities.CreateActivityRequest;
import project.gym.exception.ActivityDoesNotExistException;
import project.gym.exception.AlreadyEnrolledException;
import project.gym.exception.RoomDoesNotExistException;
import project.gym.exception.TermNotAvailableException;
import project.gym.exception.UserDoesNotExistException;
import project.gym.model.Activity;
import project.gym.model.Member;
import project.gym.model.Room;
import project.gym.repo.ActivityRepo;
import project.gym.repo.MemberRepo;
import project.gym.repo.RoomRepo;

@Service
public class ActivityService {
    private final ActivityRepo activityRepo;
    private final MemberRepo memberRepo;
    private final RoomRepo roomRepo;

    public ActivityService(ActivityRepo activityRepo, MemberRepo memberRepo, RoomRepo roomRepo) {
        this.activityRepo = activityRepo;
        this.memberRepo = memberRepo;
        this.roomRepo = roomRepo;
    }

    private boolean isTermAvailable(Activity newActivity) {
        Room room = newActivity.getRoom();
        DayOfWeek dayOfWeek = newActivity.getDayOfWeek();

        List<Activity> existingActivities = activityRepo.findByRoomAndDayOfWeek(room, dayOfWeek);
        for (Activity activity : existingActivities) {
            LocalTime startTime = activity.getStartTime();
            Short durationMin = activity.getDurationMin();
            LocalTime endTime = startTime.plusMinutes(durationMin);

            LocalTime newActivityStartTime = newActivity.getStartTime();
            Short newActivityDuration = newActivity.getDurationMin();
            LocalTime newActivityEndTime = newActivityStartTime.plusMinutes(newActivityDuration);

            boolean isUnavailable = !(newActivityStartTime.isBefore(startTime) && newActivityEndTime.isBefore(startTime))
                    && !(newActivityStartTime.isAfter(endTime) && newActivityEndTime.isAfter(endTime));

            if (isUnavailable) {
                return false;
            }
        }

        return true;
    }

    @Transactional
    public ActivityResponse createActivity(CreateActivityRequest request, Member trainer) {
        trainer = memberRepo.findById(trainer.getId()).orElseThrow(UserDoesNotExistException::new);
        Room room = roomRepo.findById(request.getRoomId()).orElseThrow(RoomDoesNotExistException::new);

        Activity newActivity = request.toActivity();
        newActivity.setRoom(room);
        newActivity.setTrainer(trainer);

        if (!isTermAvailable(newActivity)) {
            throw new TermNotAvailableException();
        }

        newActivity = activityRepo.save(newActivity);

        trainer.getTrainerActivities().add(newActivity);
        memberRepo.save(trainer);

        return ActivityResponse.valueOf(newActivity);
    }

    public Page<ActivityResponse> getActivities(Pageable pagination) {
        return activityRepo.findAll(pagination).map(ActivityResponse::valueOf);
    }

    public ActivityResponse updateActivity(Long id, CreateActivityRequest request) {
        Activity activity = activityRepo.findById(id).orElseThrow(ActivityDoesNotExistException::new);

        activity.setName(request.getName());
        activity.setDayOfWeek(request.getDayOfWeek());
        activity.setStartTime(request.getStartTime());
        activity.setDurationMin(request.getDurationMin());
        activity.setRoom(roomRepo.findById(request.getRoomId()).orElseThrow(RoomDoesNotExistException::new));

        activity = activityRepo.save(activity);
        return ActivityResponse.valueOf(activity);
    }

    public void deleteActivity(Long id) {
        activityRepo.deleteById(id);
    }

    @Transactional
    public void enrollForActivity(Long id, Member member) {
        Activity activity = activityRepo.findById(id).orElseThrow(ActivityDoesNotExistException::new);
        member = memberRepo.findById(member.getId()).orElseThrow(UserDoesNotExistException::new);

        boolean isAlreadyEnrolled = activity.getMembers().add(member);
        if (!isAlreadyEnrolled) {
            throw new AlreadyEnrolledException();
        }
        member.getActivities().add(activity);

        activityRepo.save(activity);
        memberRepo.save(member);
    }

    @Transactional
    public void leaveActivity(Long id, Member member) {
        Activity activity = activityRepo.findById(id).orElseThrow(ActivityDoesNotExistException::new);
        member = memberRepo.findById(member.getId()).orElseThrow(UserDoesNotExistException::new);

        activity.getMembers().remove(member);
        member.getActivities().remove(activity);

        activityRepo.save(activity);
        memberRepo.save(member);
    }
}
