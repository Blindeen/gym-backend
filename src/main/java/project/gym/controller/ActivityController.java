package project.gym.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import project.gym.dto.activity.ActivityResponse;
import project.gym.dto.activity.CreateActivityRequest;
import project.gym.dto.activity.UpdateActivityRequest;
import project.gym.model.Member;
import project.gym.service.ActivityService;
import project.gym.service.JwtService;

@RestController
@RequestMapping("/activities")
public class ActivityController {
    private final ActivityService activityService;
    private final JwtService jwtService;

    public ActivityController(ActivityService activityService, JwtService jwtService) {
        this.activityService = activityService;
        this.jwtService = jwtService;
    }

    @GetMapping("")
    public ResponseEntity<Page<ActivityResponse>> get(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize) {
        Pageable pagination = PageRequest.of(pageNumber - 1, pageSize);
        Page<ActivityResponse> activities = activityService.getActivities(pagination);
        return new ResponseEntity<>(activities, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<ActivityResponse> create(
            @RequestHeader("Authorization") String token,
            @RequestBody @Valid CreateActivityRequest requestBody) {
        Member trainer = jwtService.getMember(token);
        ActivityResponse newActivity = activityService.createActivity(requestBody, trainer);
        return new ResponseEntity<>(newActivity, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ActivityResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateActivityRequest requestBody) {
        ActivityResponse activity = activityService.updateActivity(id, requestBody);
        return new ResponseEntity<>(activity, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        activityService.deleteActivity(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/enrollment")
    public ResponseEntity<Void> enroll(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        Member member = jwtService.getMember(token);
        activityService.enrollForActivity(id, member);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}/leave")
    public ResponseEntity<Void> leave(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        Member member = jwtService.getMember(token);
        activityService.leaveActivity(id, member);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
