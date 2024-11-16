package project.gym;

public class Endpoints {
    public static final String SIGN_UP = "/auth/sign-up";
    public static final String SIGN_IN = "/auth/sign-in";

    public static final String CONFIRM_ACCOUNT = "/members/confirmation";
    public static final String RESET_PASSWORD = "/members/password/reset";
    public static final String CHANGE_PASSWORD = "/members/password/change";
    public static final String UPDATE_MEMBER = "/members/profile";
    public static final String UPLOAD_AVATAR = "/members/avatar";
    public static final String GET_PASS_INFO = "/members/pass";
    public static final String GET_TRAINERS = "/members/trainers";
    public static final String GET_TRAINER_ACTIVITIES = "/members/trainers/activities";
    public static final String GET_CUSTOMER_ACTIVITIES = "/members/customers/activities";

    public static final String CREATE_ACTIVITY = "/activities";
    public static final String GET_ACTIVITIES = "/activities";
    public static final String UPDATE_ACTIVITY = "/activities/{id}";
    public static final String DELETE_ACTIVITY = "/activities/{id}";
    public static final String ENROLL_ACTIVITY = "/activities/{id}/enrollment";
    public static final String LEAVE_ACTIVITY = "/activities/{id}/leave";

    public static final String PREPARE_SIGN_UP_FORM = "/forms/sign-up";
    public static final String PREPARE_EDIT_PROFILE_FORM = "/forms/edit-profile";
    public static final String PREPARE_ADD_EDIT_ACTIVITY_FORM = "/forms/add-edit-activity";
}
