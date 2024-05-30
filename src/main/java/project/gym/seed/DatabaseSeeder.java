package project.gym.seed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import project.gym.model.Room;
import project.gym.repo.RoomRepo;

@Component
public class DatabaseSeeder {
    @Autowired
    private RoomRepo roomRepo;

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seedRoomTable();
    }

    private void seedRoomTable() {
        if (roomRepo.count() != 0) {
            return;
        }

        roomRepo.save(new Room().withName("A2").withPersonLimit(10));
        roomRepo.save(new Room().withName("B5").withPersonLimit(20));
        roomRepo.save(new Room().withName("G7").withPersonLimit(30));
    }
}
