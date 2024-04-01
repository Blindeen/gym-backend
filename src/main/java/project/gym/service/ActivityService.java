package project.gym.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.gym.dto.CreateActivityDto;
import project.gym.exception.ActivityDoesNotExist;
import project.gym.model.Activity;
import project.gym.repo.ActivityRepo;

@Service
public class ActivityService {
    @Autowired
    private ActivityRepo activityRepo;

    public void create(CreateActivityDto request) {
        activityRepo.save(request.toActivity());
    }

    public void delete(Long id) {
        activityRepo.deleteById(id);
    }

    public void update(Long id, CreateActivityDto request) {
        Activity activity = activityRepo.findById(id).orElseThrow(ActivityDoesNotExist::new);

        activity.setName(request.getName() != null ? request.getName() : activity.getName());
        activity.setDayOfWeek(request.getDayOfWeek() != null ? request.getDayOfWeek() : activity.getDayOfWeek());
        activity.setStartTime(request.getStartTime() != null ? request.getStartTime() : activity.getStartTime());
        activity.setEndTime(request.getEndTime() != null ? request.getEndTime() : activity.getEndTime());

        activityRepo.save(activity);
    }
}
