package project.gym;

public class Endpoints {
    public static final String SIGN_UP = "/auth/sign-up";
    public static final String SIGN_IN = "/auth/sign-in";

    public static final String MEMBER_INFO = "/member";
    public static final String UPDATE_MEMBER = "/member/update";
    public static final String MEMBER_ACTIVITIES = "/member/activities";
    public static final String MEMBER_AVAILABLE_ACTIVITIES = "/member/available-activities";
    public static final String CONFIRM_ACCOUNT = "/member/confirm-account";
    public static final String RESET_PASSWORD = "/member/reset-password";
    public static final String CHANGE_PASSWORD = "/member/change-password";

    public static final String CREATE_ACTIVITY = "/activity/create";
    public static final String LIST_ACTIVITIES = "/activity/list";
    public static final String UPDATE_ACTIVITY = "/activity/{id}/update";
    public static final String DELETE_ACTIVITY = "/activity/{id}/delete";
    public static final String ENROLL_ACTIVITY = "/activity/{id}/enroll";
    public static final String LEAVE_ACTIVITY = "/activity/{id}/leave";

    public static final String PREPARE_SIGN_UP_FORM = "/form/sign-up/prepare";
    public static final String PREPARE_EDIT_PROFILE_FORM = "/form/edit-profile/prepare";
}
