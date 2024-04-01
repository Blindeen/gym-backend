package project.gym.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import project.gym.enums.DayOfWeek;
import project.gym.model.Activity;

import java.time.LocalTime;

@Data
public class CreateActivityDto {
    @NotEmpty(message = "Activity name is required")
    private String name;

    @NotNull(message = "Day of week is required")
    private DayOfWeek dayOfWeek;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    private LocalTime endTime;

    @NotNull(message = "Room id is required")
    private Long roomId;

    public Activity toActivity() {
        return new Activity()
                .withName(name)
                .withDayOfWeek(dayOfWeek)
                .withStartTime(startTime)
                .withEndTime(endTime);
    }
}
