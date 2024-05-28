package project.gym.dto.activity;

import lombok.Data;
import project.gym.enums.DayOfWeek;
import project.gym.model.Activity;
import project.gym.model.Room;

import java.time.LocalTime;

@Data
public class ActivityResponseDto {
    private Long id;

    private String name;

    private DayOfWeek dayOfWeek;

    private LocalTime startTime;

    private LocalTime endTime;

    private Room room;

    private TrainerDto trainer;

    public static ActivityResponseDto valueOf(Activity activity) {
        ActivityResponseDto activityResponseDto = new ActivityResponseDto();
        activityResponseDto.id = activity.getId();
        activityResponseDto.name = activity.getName();
        activityResponseDto.dayOfWeek = activity.getDayOfWeek();
        activityResponseDto.startTime = activity.getStartTime();
        activityResponseDto.endTime = activity.getEndTime();
        activityResponseDto.room = activity.getRoom();
        activityResponseDto.trainer = TrainerDto.valueOf(activity.getTrainer());

        return activityResponseDto;
    }
}
