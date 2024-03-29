package project.gym.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import project.gym.exception.UserDoesNotExistException;
import project.gym.repo.MemberRepo;

@Service
public class UserDetailsImplService implements UserDetailsService {
    @Autowired
    private MemberRepo memberRepo;

    @Override
    public UserDetails loadUserByUsername(String email) {
        return memberRepo.findByEmail(email).orElseThrow(UserDoesNotExistException::new);
    }
}
