package project.gym.repo;

import org.springframework.data.repository.CrudRepository;
import project.gym.model.Activity;

public interface ActivityRepo extends CrudRepository<Activity, Long> {
}
