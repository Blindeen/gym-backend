package project.gym.dto.activities;

import java.time.DayOfWeek;
import java.time.LocalTime;

public interface CustomerActivity {
    String getName();

    DayOfWeek getDayOfWeek();

    LocalTime getStartTime();

    String getDurationMin();

    String getRoomName();

    String getTrainerFirstName();

    String getTrainerLastName();

    boolean isEnrolled();
}
