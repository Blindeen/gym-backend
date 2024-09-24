package project.gym.enums;

import org.apache.commons.lang3.StringUtils;

public enum PassType {
    COLLEGE,
    SENIOR,
    DYNAMIC,
    UNLIMITED,
    YEARLY;

    @Override
    public String toString() {
        String lowercaseName = super.toString().toLowerCase();
        return StringUtils.capitalize(lowercaseName);
    }
}
