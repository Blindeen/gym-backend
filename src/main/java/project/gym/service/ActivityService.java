package project.gym.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.gym.config.JwtService;
import project.gym.dto.ActivityResponseDto;
import project.gym.dto.CreateActivityDto;
import project.gym.exception.ActivityDoesNotExist;
import project.gym.exception.RoomDoesNotExist;
import project.gym.model.Activity;
import project.gym.model.Member;
import project.gym.model.Room;
import project.gym.repo.ActivityRepo;
import project.gym.repo.RoomRepo;


@Service
public class ActivityService {
    @Autowired
    private ActivityRepo activityRepo;

    @Autowired
    private RoomRepo roomRepo;

    @Autowired
    private JwtService jwtService;

    public void create(CreateActivityDto request, String token) {
        Activity newActivity = request.toActivity();
        Member trainer = jwtService.getMember(token);
        Room room = roomRepo.findById(request.getRoomId()).orElseThrow(RoomDoesNotExist::new);

        newActivity.setRoom(room);
        newActivity.setTrainer(trainer);
        activityRepo.save(newActivity);
    }

    public Page<ActivityResponseDto> read(Pageable pageable) {
        return activityRepo.findAll(pageable).map(ActivityResponseDto::valueOf);
    }

    @Transactional
    public void update(Long id, CreateActivityDto request) {
        Activity activity = activityRepo.findById(id).orElseThrow(ActivityDoesNotExist::new);

        activity.setName(request.getName() != null ? request.getName() : activity.getName());
        activity.setDayOfWeek(request.getDayOfWeek() != null ? request.getDayOfWeek() : activity.getDayOfWeek());
        activity.setStartTime(request.getStartTime() != null ? request.getStartTime() : activity.getStartTime());
        activity.setEndTime(request.getEndTime() != null ? request.getEndTime() : activity.getEndTime());
        if (request.getRoomId() != null) {
            activity.setRoom(roomRepo.findById(request.getRoomId()).orElseThrow(RoomDoesNotExist::new));
        }

        activityRepo.save(activity);
    }

    public void delete(Long id) {
        activityRepo.deleteById(id);
    }
}
