package project.gym.repo;

import org.springframework.data.repository.CrudRepository;
import project.gym.model.Room;

public interface RoomRepo extends CrudRepository<Room, Long> {
}
