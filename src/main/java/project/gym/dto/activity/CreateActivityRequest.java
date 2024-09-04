package project.gym.dto.activity;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import project.gym.enums.DayOfWeek;
import project.gym.model.Activity;

import java.time.LocalTime;

@Data
public class CreateActivityRequest {
    @NotEmpty(message = "{validation.activityNameIsRequired}")
    private String name;

    @NotNull(message = "{validation.dayOfWeekIsRequired}")
    private DayOfWeek dayOfWeek;

    @NotNull(message = "{validation.startTimeIsRequired}")
    private LocalTime startTime;

    @NotNull(message = "{validation.endTimeIsRequired}")
    private LocalTime endTime;

    @NotNull(message = "{validation.roomIdIsRequired}")
    private Long roomId;

    public Activity toActivity() {
        return new Activity()
                .withName(name)
                .withDayOfWeek(dayOfWeek)
                .withStartTime(startTime)
                .withEndTime(endTime);
    }
}
