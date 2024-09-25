package project.gym.repo;

import org.springframework.data.repository.CrudRepository;

import project.gym.model.Image;

public interface ImageRepo extends CrudRepository<Image, Long> {

}
