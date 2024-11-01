package project.gym.service;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import project.gym.Utils;
import project.gym.dto.activities.ActivityResponse;
import project.gym.dto.cloudinary.UploadImageResponse;
import project.gym.dto.members.ConfirmAccountRequest;
import project.gym.dto.members.UpdateMemberRequest;
import project.gym.dto.members.pass.PassBasics;
import project.gym.dto.members.password.ChangePasswordRequest;
import project.gym.dto.members.password.ResetPasswordRequest;
import project.gym.dto.members.trainers.TrainerInfo;
import project.gym.enums.Role;
import project.gym.exception.members.confirmation.AccountAlreadyConfirmed;
import project.gym.exception.members.confirmation.InvalidTokenException;
import project.gym.exception.members.confirmation.TokenExpiredException;
import project.gym.model.AccountConfirmation;
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
    private final CloudinaryService cloudinaryService;
    private final PasswordEncoder passwordEncoder;
    private final Utils utils;

    public MemberService(
            MemberRepo memberRepo,
            ActivityRepo activityRepo,
            AccountConfirmationRepo accountConfirmationRepo,
            PasswordResetRepo passwordResetRepo,
            GoogleWalletService googleWalletService,
            CloudinaryService cloudinaryService,
            PasswordEncoder passwordEncoder,
            Utils utils) {
        this.memberRepo = memberRepo;
        this.activityRepo = activityRepo;
        this.accountConfirmationRepo = accountConfirmationRepo;
        this.passwordResetRepo = passwordResetRepo;

        this.googleWalletService = googleWalletService;
        this.cloudinaryService = cloudinaryService;
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
        Optional<Member> optionalMember = memberRepo.findByEmail(email);
        if (optionalMember.isEmpty()) {
            return null;
        }

        Member member = optionalMember.get();

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

    public Member update(Member member, UpdateMemberRequest request) {
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

        return memberRepo.save(member);
    }

    public UploadImageResponse uploadAvatar(Member member, MultipartFile file) {
        String publicID = "member_" + member.getId();
        String avatarURL = null;
        try {
            avatarURL = cloudinaryService.uploadImage(file, publicID);
        } catch (IOException e) {
            e.printStackTrace();
        }

        member.setAvatarURL(avatarURL);
        memberRepo.save(member);

        return new UploadImageResponse(avatarURL);
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

    public List<TrainerInfo> getTrainers() {
        return memberRepo.findByRole(Role.TRAINER)
                .stream().map(TrainerInfo::valueOf).toList();
    }
}
