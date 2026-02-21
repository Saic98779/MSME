package com.metaverse.msme.activity;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ActivityLogService {

    private final ActivityLogRepository repository;

    public ActivityLogService(ActivityLogRepository repository) {
        this.repository = repository;
    }

    public ActivityLog log(
            String actionType,
            String actionDescription,
            String requestPayload,
            String status,
            String errorMessage,
            HttpServletRequest request) {

        ActivityLog entry = ActivityLog.builder()
                .userId(safeUserId(request))
                .username(safeUsername(request))
                .role(safeRole(request))
                .actionType(defaultActionType(actionType))
                .actionDescription(actionDescription)
                .requestPayload(requestPayload)
                .status(defaultStatus(status))
                .errorMessage(errorMessage)
                .url(requestUrl(request))
                .build();

        return repository.save(entry);
    }

    public ActivityLog logSuccess(String actionType, String actionDescription, String requestPayload, HttpServletRequest request) {
        return log(actionType, actionDescription, requestPayload, "SUCCESS", null, request);
    }

    public ActivityLog logFailure(String actionType, String actionDescription, String requestPayload, String errorMessage, HttpServletRequest request) {
        return log(actionType, actionDescription, requestPayload, "FAILURE", errorMessage, request);
    }

    private String requestUrl(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        StringBuilder url = new StringBuilder(request.getRequestURL());
        String qs = request.getQueryString();
        if (StringUtils.hasText(qs)) {
            url.append('?').append(qs);
        }
        return url.toString();
    }

    private String defaultStatus(String status) {
        return StringUtils.hasText(status) ? status : "SUCCESS";
    }

    private String defaultActionType(String actionType) {
        return StringUtils.hasText(actionType) ? actionType : "UNKNOWN";
    }

    private String safeUserId(HttpServletRequest request) {
        // Prefer explicit attribute set by JwtAuthenticationFilter
        if (request != null) {
            Object attr = request.getAttribute("userId");
            if (attr != null) {
                return attr.toString();
            }
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getName() != null) {
            return auth.getName();
        }
        return "UNKNOWN_USER";
    }

    private String safeUsername(HttpServletRequest request) {
        if (request != null) {
            Object username = request.getAttribute("username");
            if (username != null) {
                return username.toString();
            }
            Object email = request.getAttribute("email");
            if (email != null) {
                return email.toString();
            }
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() != null) {
            return auth.getPrincipal().toString();
        }
        return "UNKNOWN_USERNAME";
    }

    private String safeRole(HttpServletRequest request) {
        if (request != null) {
            Object role = request.getAttribute("userRole");
            if (role != null) {
                return role.toString();
            }
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities() != null) {
            for (GrantedAuthority authority : auth.getAuthorities()) {
                String authName = authority.getAuthority();
                if (StringUtils.hasText(authName)) {
                    // Strip common prefix if present
                    return authName.startsWith("ROLE_") ? authName.substring(5) : authName;
                }
            }
        }
        return "UNKNOWN_ROLE";
    }
}
