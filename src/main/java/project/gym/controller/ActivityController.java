package project.gym.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.gym.dto.ActivityResponseDto;
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
            @RequestBody @Valid CreateActivityDto requestBody
    ) {
        ActivityResponseDto newActivity = activityService.create(requestBody, token);
        return new ResponseEntity<>(newActivity, HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<Object> read(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "5") int pageSize) {
        Pageable pageableRequest = PageRequest.of(pageNumber, pageSize);
        Page<ActivityResponseDto> activities = activityService.read(pageableRequest);
        return new ResponseEntity<>(activities, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        activityService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody CreateActivityDto requestBody) {
        ActivityResponseDto activity = activityService.update(id, requestBody);
        return new ResponseEntity<>(activity, HttpStatus.OK);
    }
}
