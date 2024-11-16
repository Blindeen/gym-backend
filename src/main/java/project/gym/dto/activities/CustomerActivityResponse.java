package project.gym.dto.activities;

import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import lombok.Data;

@Data
public class CustomerActivityResponse {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    private Long id;
    private String name;
    private String dayOfWeek;
    private String startTime;
    private String durationMin;
    private String roomName;
    private String trainer;
    private boolean enrolled;

    public static CustomerActivityResponse valueOf(CustomerActivity customerActivity) {
        CustomerActivityResponse customerActivityResponse = new CustomerActivityResponse();
        customerActivityResponse.id = customerActivity.getId();
        customerActivityResponse.name = customerActivity.getName();

        DayOfWeek dayOfWeek = customerActivity.getDayOfWeek();
        String dayOfWeekString = dayOfWeek.getDisplayName(TextStyle.FULL, LocaleContextHolder.getLocale());
        customerActivityResponse.dayOfWeek = StringUtils.capitalize(dayOfWeekString);

        customerActivityResponse.startTime = customerActivity.getStartTime().format(formatter);
        customerActivityResponse.durationMin = customerActivity.getDurationMin().toString();
        customerActivityResponse.roomName = customerActivity.getRoomName();
        customerActivityResponse.trainer = customerActivity.getTrainer();
        customerActivityResponse.enrolled = customerActivity.isEnrolled();

        return customerActivityResponse;
    }
}
