package project.gym.dto;

import lombok.Data;
import project.gym.model.Member;

@Data
public class TrainerDto {
    private String firstName;
    private String lastName;

    public static TrainerDto valueOf(Member trainer) {
        TrainerDto trainerDto = new TrainerDto();
        trainerDto.firstName = trainer.getFirstName();
        trainerDto.lastName = trainer.getLastName();

        return trainerDto;
    }
}
