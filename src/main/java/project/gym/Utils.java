package project.gym;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.keygen.StringKeyGenerator;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class Utils {
    private final MessageSource messageSource;

    public Utils(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getBrowser(HttpServletRequest request) {
        String browser = messageSource.getMessage("browser.unknown", null, LocaleContextHolder.getLocale());

        if (request == null) {
            return browser;
        }

        String userAgentHeader = request.getHeader("User-Agent");
        if (userAgentHeader == null || "".equals(userAgentHeader)) {
            return browser;
        }

        if (userAgentHeader.contains("Chrome")) {
            browser = "Google Chrome";
        } else if (userAgentHeader.contains("Firefox")) {
            browser = "Mozilla Firefox";
        } else if (userAgentHeader.contains("Safari") && !userAgentHeader.contains("Chrome")) {
            browser = "Apple Safari";
        } else if (userAgentHeader.contains("MSIE") || userAgentHeader.contains("Trident")) {
            browser = "Internet Explorer";
        } else if (userAgentHeader.contains("Edge")) {
            browser = "Microsoft Edge";
        }

        return browser;
    }

    public String getClientAddr(HttpServletRequest request) {
        String clientAddr = messageSource.getMessage("ipAddress.unknown", null, LocaleContextHolder.getLocale());

        if (request == null) {
            return clientAddr;
        }

        String xForwardedForHeader = request.getHeader("X-FORWARDED-FOR");
        if (xForwardedForHeader == null || "".equals(xForwardedForHeader)) {
            clientAddr = request.getRemoteAddr();
        } else {
            clientAddr = xForwardedForHeader;
        }

        return clientAddr;
    }

    public String getServerHostName(HttpServletRequest request) {
        return request.getServerName();
    }

    public ZonedDateTime getUtcDateTime() {
        Instant instant = Instant.ofEpochMilli(System.currentTimeMillis());
        ZonedDateTime utcDateTime = instant.atZone(ZoneOffset.UTC);
        return utcDateTime;
    }

    public String formatDateTime(ZonedDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return dateTime.format(formatter);
    }

    public String generateUniqueToken() {
        StringKeyGenerator stringKeyGenerator = KeyGenerators.string();
        return stringKeyGenerator.generateKey();
    }
}
