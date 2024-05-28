package project.gym.dto.authentication;

import lombok.Data;
import project.gym.model.Member;

@Data
public class AuthenticationResponseDto {
    private MemberDto user;
    private String token;

    public AuthenticationResponseDto(Member member, String token) {
        user = MemberDto.valueOf(member);
        this.token = token;
    }
}
