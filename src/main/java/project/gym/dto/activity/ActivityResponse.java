package project.gym.dto.activity;

import lombok.Data;
import project.gym.model.Activity;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

@Data
public class ActivityResponse {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    private Long id;
    private String name;
    private String dayOfWeek;
    private String startTime;
    private Short durationMin;
    private String room;
    private TrainerInfo trainer;

    public static ActivityResponse valueOf(Activity activity) {
        ActivityResponse activityResponse = new ActivityResponse();
        activityResponse.id = activity.getId();
        activityResponse.name = activity.getName();

        DayOfWeek dayOfWeek = activity.getDayOfWeek();
        String dayOfWeekString = dayOfWeek.getDisplayName(TextStyle.FULL, LocaleContextHolder.getLocale());
        activityResponse.dayOfWeek = StringUtils.capitalize(dayOfWeekString);

        activityResponse.startTime = activity.getStartTime().format(formatter);

        activityResponse.durationMin = activity.getDurationMin();
        activityResponse.room = activity.getRoom().getName();
        activityResponse.trainer = TrainerInfo.valueOf(activity.getTrainer());

        return activityResponse;
    }
}
