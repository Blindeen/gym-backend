package project.gym.dto.activities;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import project.gym.model.Activity;

import java.time.LocalTime;
import java.time.DayOfWeek;

@Data
public class CreateActivityRequest {
    @NotEmpty(message = "{validation.activityNameIsRequired}")
    private String name;

    @NotNull(message = "{validation.dayOfWeekIsRequired}")
    private DayOfWeek dayOfWeek;

    @NotNull(message = "{validation.startTimeIsRequired}")
    private LocalTime startTime;

    @NotNull(message = "{validation.durationIsRequired}")
    private Short durationMin;

    @NotNull(message = "{validation.roomIdIsRequired}")
    private Long roomId;

    public Activity toActivity() {
        return new Activity()
                .withName(name)
                .withDayOfWeek(dayOfWeek)
                .withStartTime(startTime)
                .withDurationMin(durationMin);
    }
}
