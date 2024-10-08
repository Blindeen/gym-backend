package project.gym.dto.pass;

import lombok.Data;
import project.gym.model.Pass;

@Data
public class PassBasics {
    private String name;
    private String uuid;
    private String googleWalletPassToken;

    public static PassBasics valueOf(Pass pass) {
        PassBasics passBasics = new PassBasics();
        passBasics.setName(pass.getType().getName());
        passBasics.setUuid(pass.getUuid());
        return passBasics;
    }
}
