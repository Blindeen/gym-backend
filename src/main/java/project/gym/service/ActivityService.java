package project.gym.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.gym.dto.activity.ActivityResponseDto;
import project.gym.dto.activity.CreateActivityDto;
import project.gym.exception.ActivityDoesNotExist;
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

    public ActivityResponseDto createActivity(CreateActivityDto request, Member trainer) {
        Activity newActivity = request.toActivity();
        Room room = roomRepo.findById(request.getRoomId()).orElseThrow(RoomDoesNotExist::new);

        newActivity.setRoom(room);
        newActivity.setTrainer(trainer);
        newActivity = activityRepo.save(newActivity);
        return ActivityResponseDto.valueOf(newActivity);
    }

    public Page<ActivityResponseDto> listActivities(Pageable pageable) {
        return activityRepo.findAll(pageable).map(ActivityResponseDto::valueOf);
    }

    public ActivityResponseDto updateActivity(Long id, CreateActivityDto request) {
        Activity activity = activityRepo.findById(id).orElseThrow(ActivityDoesNotExist::new);

        activity.setName(request.getName() != null ? request.getName() : activity.getName());
        activity.setDayOfWeek(request.getDayOfWeek() != null ? request.getDayOfWeek() : activity.getDayOfWeek());
        activity.setStartTime(request.getStartTime() != null ? request.getStartTime() : activity.getStartTime());
        activity.setEndTime(request.getEndTime() != null ? request.getEndTime() : activity.getEndTime());
        if (request.getRoomId() != null) {
            activity.setRoom(roomRepo.findById(request.getRoomId()).orElseThrow(RoomDoesNotExist::new));
        }

        activity = activityRepo.save(activity);
        return ActivityResponseDto.valueOf(activity);
    }

    public void deleteActivity(Long id) {
        activityRepo.deleteById(id);
    }

    @Transactional
    public void enrollForActivity(Long id, Member member) {
        Activity activity = activityRepo.findById(id).orElseThrow(ActivityDoesNotExist::new);
        member = memberRepo.findById(member.getId()).orElseThrow(UserDoesNotExistException::new);

        activity.getMembers().add(member);
        member.getActivities().add(activity);

        activityRepo.save(activity);
        memberRepo.save(member);
    }
}
