package project.gym.dto;

import lombok.Data;
import project.gym.enums.DayOfWeek;
import project.gym.model.Activity;

import java.time.LocalTime;

@Data
public class ActivityResponseDto {
    private String name;

    private DayOfWeek dayOfWeek;

    private LocalTime startTime;

    private LocalTime endTime;

    private Long room;

    private TrainerDto trainer;

    public static ActivityResponseDto valueOf(Activity activity) {
        ActivityResponseDto activityResponseDto = new ActivityResponseDto();
        activityResponseDto.name = activity.getName();
        activityResponseDto.dayOfWeek = activity.getDayOfWeek();
        activityResponseDto.startTime = activity.getStartTime();
        activityResponseDto.endTime = activity.getEndTime();
        activityResponseDto.room = activity.getRoom().getNumber();
        activityResponseDto.trainer = TrainerDto.valueOf(activity.getTrainer());

        return activityResponseDto;
    }
}
