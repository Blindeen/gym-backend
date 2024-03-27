package project.gym.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.gym.dto.RegisterMemberDto;
import project.gym.model.Contact;
import project.gym.model.Member;
import project.gym.repo.MemberRepo;

@Service
public class MemberService {
    @Autowired
    private MemberRepo memberRepo;

    public void createMember(RegisterMemberDto registerMemberDto) {
        Member newMember = registerMemberDto.toMember();
        Contact newContact = registerMemberDto.toContact();

        newMember.setContact(newContact);
        memberRepo.save(newMember);
    }
}
