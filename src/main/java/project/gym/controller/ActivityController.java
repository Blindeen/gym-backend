package project.gym.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.gym.model.Activity;
import project.gym.repo.ActivityRepo;

@RestController
@RequestMapping("/api/activity")
public class ActivityController {
    @Autowired
    private ActivityRepo activityRepo;

    @PostMapping("/create")
    Activity create(@RequestBody Activity newActivity) {
        return activityRepo.save(newActivity);
    }
}
