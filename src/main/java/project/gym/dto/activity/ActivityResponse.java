package project.gym.dto.activity;

import lombok.Data;
import project.gym.enums.DayOfWeek;
import project.gym.model.Activity;
import project.gym.model.Room;

import java.time.LocalTime;

@Data
public class ActivityResponse {
    private Long id;

    private String name;

    private DayOfWeek dayOfWeek;

    private LocalTime startTime;

    private LocalTime endTime;

    private Room room;

    private TrainerInfo trainer;

    public static ActivityResponse valueOf(Activity activity) {
        ActivityResponse activityResponse = new ActivityResponse();
        activityResponse.id = activity.getId();
        activityResponse.name = activity.getName();
        activityResponse.dayOfWeek = activity.getDayOfWeek();
        activityResponse.startTime = activity.getStartTime();
        activityResponse.endTime = activity.getEndTime();
        activityResponse.room = activity.getRoom();
        activityResponse.trainer = TrainerInfo.valueOf(activity.getTrainer());

        return activityResponse;
    }
}
