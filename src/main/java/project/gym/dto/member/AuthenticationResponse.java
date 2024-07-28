package project.gym.dto.member;

import lombok.Data;
import project.gym.model.Member;

@Data
public class AuthenticationResponse {
    private MemberInfo user;
    private String token;

    public AuthenticationResponse(Member member, String token) {
        user = MemberInfo.valueOf(member);
        this.token = token;
    }
}
