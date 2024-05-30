package project.gym.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.gym.config.JwtService;
import project.gym.dto.activity.ActivityResponse;
import project.gym.dto.activity.CreateActivityRequest;
import project.gym.model.Member;
import project.gym.service.ActivityService;

@RestController
@RequestMapping("/activity")
public class ActivityController {
    @Autowired
    private ActivityService activityService;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/create")
    public ResponseEntity<Object> create(
            @RequestHeader("Authorization") String token,
            @RequestBody @Valid CreateActivityRequest requestBody
    ) {
        Member trainer = jwtService.getMember(token);
        ActivityResponse newActivity = activityService.createActivity(requestBody, trainer);
        return new ResponseEntity<>(newActivity, HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<Object> list(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize
    ) {
        Pageable pageableRequest = PageRequest.of(pageNumber, pageSize);
        Page<ActivityResponse> activities = activityService.listActivities(pageableRequest);
        return new ResponseEntity<>(activities, HttpStatus.OK);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody CreateActivityRequest requestBody) {
        ActivityResponse activity = activityService.updateActivity(id, requestBody);
        return new ResponseEntity<>(activity, HttpStatus.OK);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        activityService.deleteActivity(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}/enroll")
    public ResponseEntity<Object> enroll(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token
    ) {
        Member member = jwtService.getMember(token);
        activityService.enrollForActivity(id, member);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}/leave")
    public ResponseEntity<Object> leave(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token
    ) {
        Member member = jwtService.getMember(token);
        activityService.leaveActivity(id, member);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
