package project.gym.dto.activities;

import java.time.DayOfWeek;
import java.time.LocalTime;

public interface CustomerActivity {
    Long getId();
    String getName();
    DayOfWeek getDayOfWeek();
    LocalTime getStartTime();
    String getDurationMin();
    String getRoomName();
    String getTrainer();
    boolean isEnrolled();
}
