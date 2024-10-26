package project.gym.dto.form;

import lombok.Data;
import project.gym.model.Room;

@Data
public class AddEditActivityFormData {
    private final Iterable<Room> rooms;
}
