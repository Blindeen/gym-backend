package project.gym.dto.members.trainers;

import lombok.Data;
import project.gym.model.Member;

@Data
public class TrainerInfo {
    private final String firstName;
    private final String lastName;
    private final String avatarURL;

    public static TrainerInfo valueOf(Member member) {
        return new TrainerInfo(
                member.getFirstName(),
                member.getLastName(),
                member.getAvatarURL()
        );
    }
}
