package project.gym.dto.activity;

import lombok.Data;
import project.gym.model.Member;

@Data
public class TrainerInfo {
    private String firstName;
    private String lastName;

    public static TrainerInfo valueOf(Member trainer) {
        TrainerInfo trainerInfo = new TrainerInfo();
        trainerInfo.firstName = trainer.getFirstName();
        trainerInfo.lastName = trainer.getLastName();

        return trainerInfo;
    }
}
