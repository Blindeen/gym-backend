package project.gym.service;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import project.gym.Utils;
import project.gym.dto.activity.ActivityResponse;
import project.gym.dto.member.ChangePasswordRequest;
import project.gym.dto.member.ConfirmAccountRequest;
import project.gym.dto.member.ResetPasswordRequest;
import project.gym.dto.member.UpdateMemberRequest;
import project.gym.dto.pass.PassBasics;
import project.gym.enums.Role;
import project.gym.exception.AccountAlreadyConfirmed;
import project.gym.exception.InvalidTokenException;
import project.gym.exception.TokenExpiredException;
import project.gym.exception.UserDoesNotExistException;
import project.gym.model.AccountConfirmation;
import project.gym.model.Image;
import project.gym.model.Member;
import project.gym.model.Pass;
import project.gym.model.PasswordReset;
import project.gym.repo.AccountConfirmationRepo;
import project.gym.repo.ActivityRepo;
import project.gym.repo.MemberRepo;
import project.gym.repo.PasswordResetRepo;

@Service
public class MemberService {
    private final MemberRepo memberRepo;
    private final ActivityRepo activityRepo;
    private final AccountConfirmationRepo accountConfirmationRepo;
    private final PasswordResetRepo passwordResetRepo;

    private final GoogleWalletService googleWalletService;
    private final PasswordEncoder passwordEncoder;
    private final Utils utils;

    public MemberService(
            MemberRepo memberRepo,
            ActivityRepo activityRepo,
            AccountConfirmationRepo accountConfirmationRepo,
            PasswordResetRepo passwordResetRepo,
            GoogleWalletService googleWalletService,
            PasswordEncoder passwordEncoder,
            Utils utils) {
        this.memberRepo = memberRepo;
        this.activityRepo = activityRepo;
        this.accountConfirmationRepo = accountConfirmationRepo;
        this.passwordResetRepo = passwordResetRepo;

        this.googleWalletService = googleWalletService;
        this.passwordEncoder = passwordEncoder;
        this.utils = utils;
    }

    public void confirmAccount(ConfirmAccountRequest request) {
        String confirmAccountToken = request.getToken();
        AccountConfirmation accountConfirmation = accountConfirmationRepo.findByToken(confirmAccountToken)
                .orElseThrow(InvalidTokenException::new);

        boolean isConfirmed = accountConfirmation.isConfirmed();
        Instant expiresAt = accountConfirmation.getExpiresAt();
        if (isConfirmed) {
            throw new AccountAlreadyConfirmed();
        } else if (expiresAt.isBefore(Instant.now())) {
            throw new TokenExpiredException();
        }

        accountConfirmation.setConfirmed(true);
        accountConfirmation.setConfirmedAt(Instant.now());
        accountConfirmationRepo.save(accountConfirmation);
    }

    public PasswordReset resetPassword(ResetPasswordRequest request) {
        String email = request.getEmail();
        Member member = memberRepo.findByEmail(email).orElseThrow(UserDoesNotExistException::new);

        PasswordReset passwordReset = member.getPasswordReset();
        if (passwordReset == null) {
            passwordReset = new PasswordReset();
        }

        passwordReset.setToken(utils.generateUniqueToken());
        passwordReset.setLastAttemptAt(Instant.now());
        passwordReset.setMember(member);
        Instant expiresAt = Instant.now().plus(PasswordReset.EXPIRY_TIME, ChronoUnit.MINUTES);
        passwordReset.setExpiresAt(expiresAt);

        member.setPasswordReset(passwordReset);

        passwordResetRepo.save(passwordReset);
        memberRepo.save(member);

        return passwordReset;
    }

    public void changePassword(ChangePasswordRequest request) {
        String token = request.getToken();
        PasswordReset passwordReset = passwordResetRepo.findByToken(token).orElseThrow(InvalidTokenException::new);
        String password = request.getPassword();

        Member member = passwordReset.getMember();
        member.setPassword(passwordEncoder.encode(password));
        memberRepo.save(member);
    }

    public Member update(Member member, UpdateMemberRequest request, MultipartFile profilePicture) {
        member.setFirstName(request.getFirstName());
        member.setLastName(request.getLastName());
        member.setContact(request.toContact());

        String newPassword = request.getNewPassword();
        if (newPassword != null && !"".equals(newPassword)) {
            String currentPassword = request.getPassword();
            boolean currentMatchesOld = passwordEncoder.matches(currentPassword, member.getPassword());
            if (!currentMatchesOld) {
                throw new BadCredentialsException("Incorrect old password");
            }

            member.setPassword(passwordEncoder.encode(newPassword));
        }

        if (profilePicture != null) {
            createUpdateProfilePicture(member, profilePicture);
        }

        return memberRepo.save(member);
    }

    private void createUpdateProfilePicture(Member member, MultipartFile profilePicture) {
        Image profilePictureImage = member.getProfilePicture();
        if (profilePictureImage == null) {
            profilePictureImage = Image.valueOf(profilePicture);
        } else {
            profilePictureImage.setName(profilePicture.getOriginalFilename());
            profilePictureImage.setType(profilePicture.getContentType());
            try {
                profilePictureImage.setData(profilePicture.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        member.setProfilePicture(profilePictureImage);
    }

    public PassBasics getPassBasics(Member member) {
        Pass pass = member.getPass();

        PassBasics passBasics = PassBasics.valueOf(pass);
        String googleWalletToken;
        try {
            googleWalletToken = googleWalletService.generateGoogleWalletPass(
                    "member_" + member.getId(),
                    passBasics.getUuid(),
                    member.getFirstName(),
                    member.getLastName());
        } catch (IOException e) {
            googleWalletToken = "";
        }
        passBasics.setGoogleWalletPassToken(googleWalletToken);

        return passBasics;
    }

    public Page<ActivityResponse> getMyActivities(String name, Member member, Pageable pagination) {
        Role role = member.getRole();
        if (role == Role.TRAINER) {
            return activityRepo.findByTrainerAndNameContains(member, name, pagination)
                    .map(ActivityResponse::valueOf);
        } else {
            return activityRepo.findByMembersContainsAndNameContains(member, name, pagination)
                    .map(ActivityResponse::valueOf);
        }
    }

    public List<ActivityResponse> getAvailableActivities(Member member) {
        return activityRepo.findByMembersNotContains(member)
                .stream().map(ActivityResponse::valueOf).toList();
    }
}
