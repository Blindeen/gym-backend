package project.gym.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.gym.dto.CreateActivityDto;
import project.gym.service.ActivityService;

@RestController
@RequestMapping("/api/activity")
public class ActivityController {
    @Autowired
    private ActivityService activityService;

    @PostMapping("/create")
    public ResponseEntity<Object> create(
            @RequestHeader("Authorization") String token,
            @RequestBody @Valid CreateActivityDto request
    ) {
        activityService.create(request, token);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        activityService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody CreateActivityDto request) {
        activityService.update(id, request);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
