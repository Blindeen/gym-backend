package project.gym.dto.auth;

import lombok.Data;
import project.gym.model.Member;

@Data
public class AuthenticationResponse {
    private MemberInfo user;
    private String accessToken;
    private String refreshToken;

    public AuthenticationResponse(Member member, String accessToken, String refreshToken) {
        user = MemberInfo.valueOf(member);
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
