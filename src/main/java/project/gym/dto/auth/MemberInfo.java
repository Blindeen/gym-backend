package project.gym.dto.auth;

import lombok.Data;
import project.gym.model.Member;

@Data
public class MemberInfo {
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String avatarURL;

    public static MemberInfo valueOf(Member member) {
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.firstName = member.getFirstName();
        memberInfo.lastName = member.getLastName();
        memberInfo.email = member.getEmail();
        memberInfo.role = member.getRole().toString();
        memberInfo.avatarURL = member.getAvatarURL();

        return memberInfo;
    }
}
