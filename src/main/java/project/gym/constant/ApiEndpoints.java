package project.gym.constant;

public class ApiEndpoints {
    public static final String REGISTER = "/member/register";
    public static final String LOGIN = "/member/login";
    public static final String MEMBER_INFO = "/member";
    public static final String UPDATE_MEMBER = "/member/update";
    public static final String MEMBER_ACTIVITIES = "/member/activities";
    public static final String MEMBER_AVAILABLE_ACTIVITIES = "/member/available-activities";

    public static final String CREATE_ACTIVITY = "/activity/create";
    public static final String LIST_ACTIVITIES = "/activity/list";
    public static final String UPDATE_ACTIVITY = "/activity/{id}/update";
    public static final String DELETE_ACTIVITY = "/activity/{id}/delete";
    public static final String ENROLL_ACTIVITY = "/activity/{id}/enroll";
    public static final String LEAVE_ACTIVITY = "/activity/{id}/leave";
}
