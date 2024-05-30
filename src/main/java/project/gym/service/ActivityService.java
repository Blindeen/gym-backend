package project.gym.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.gym.dto.activity.ActivityResponse;
import project.gym.dto.activity.CreateActivityRequest;
import project.gym.exception.ActivityDoesNotExist;
import project.gym.exception.AlreadyEnrolledException;
import project.gym.exception.RoomDoesNotExist;
import project.gym.exception.UserDoesNotExistException;
import project.gym.model.Activity;
import project.gym.model.Member;
import project.gym.model.Room;
import project.gym.repo.ActivityRepo;
import project.gym.repo.MemberRepo;
import project.gym.repo.RoomRepo;

@Service
public class ActivityService {
    @Autowired
    private ActivityRepo activityRepo;

    @Autowired
    private MemberRepo memberRepo;

    @Autowired
    private RoomRepo roomRepo;

    @Transactional
    public ActivityResponse createActivity(CreateActivityRequest request, Member trainer) {
        trainer = memberRepo.findById(trainer.getId()).orElseThrow(UserDoesNotExistException::new);
        Room room = roomRepo.findById(request.getRoomId()).orElseThrow(RoomDoesNotExist::new);

        Activity newActivity = request.toActivity();
        newActivity.setRoom(room);
        newActivity.setTrainer(trainer);
        newActivity = activityRepo.save(newActivity);

        trainer.getTrainerActivities().add(newActivity);
        memberRepo.save(trainer);

        return ActivityResponse.valueOf(newActivity);
    }

    public Page<ActivityResponse> getActivities(Pageable pagination) {
        return activityRepo.findAll(pagination).map(ActivityResponse::valueOf);
    }

    public ActivityResponse updateActivity(Long id, CreateActivityRequest request) {
        Activity activity = activityRepo.findById(id).orElseThrow(ActivityDoesNotExist::new);

        activity.setName(request.getName());
        activity.setDayOfWeek(request.getDayOfWeek());
        activity.setStartTime(request.getStartTime());
        activity.setEndTime(request.getEndTime());
        activity.setRoom(roomRepo.findById(request.getRoomId()).orElseThrow(RoomDoesNotExist::new));

        activity = activityRepo.save(activity);
        return ActivityResponse.valueOf(activity);
    }

    public void deleteActivity(Long id) {
        activityRepo.deleteById(id);
    }

    @Transactional
    public void enrollForActivity(Long id, Member member) {
        Activity activity = activityRepo.findById(id).orElseThrow(ActivityDoesNotExist::new);
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
        Activity activity = activityRepo.findById(id).orElseThrow(ActivityDoesNotExist::new);
        member = memberRepo.findById(member.getId()).orElseThrow(UserDoesNotExistException::new);

        activity.getMembers().remove(member);
        member.getActivities().remove(activity);

        activityRepo.save(activity);
        memberRepo.save(member);
    }
}
