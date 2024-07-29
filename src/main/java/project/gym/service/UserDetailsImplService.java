package project.gym.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import project.gym.exception.UserDoesNotExistException;
import project.gym.repo.MemberRepo;

@Service
public class UserDetailsImplService implements UserDetailsService {
    private final MemberRepo memberRepo;

    public UserDetailsImplService(MemberRepo memberRepo) {
        this.memberRepo = memberRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        return memberRepo.findByEmail(email).orElseThrow(UserDoesNotExistException::new);
    }
}
