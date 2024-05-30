package project.gym.dto.member;

import lombok.Data;
import project.gym.model.Member;

@Data
public class MemberInfo {
    private String email;
    private String role;

    public static MemberInfo valueOf(Member member) {
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.email = member.getEmail();
        memberInfo.role = member.getRole().toString();

        return memberInfo;
    }
}
