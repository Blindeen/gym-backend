package project.gym.dto.authentication;

import lombok.Data;
import project.gym.model.Member;

@Data
public class MemberDto {
    private String email;
    private String role;

    public static MemberDto valueOf(Member member) {
        MemberDto memberDto = new MemberDto();
        memberDto.email = member.getEmail();
        memberDto.role = member.getRole().toString();

        return memberDto;
    }
}
