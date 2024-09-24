package project.gym.enums;

import org.apache.commons.lang3.StringUtils;

public enum PaymentMethodType {
    CARD,
    CASH,
    PAYPAL,
    BLIK;

    @Override
    public String toString() {
        String lowercaseName = super.toString().toLowerCase();
        return StringUtils.capitalize(lowercaseName);
    }
}
