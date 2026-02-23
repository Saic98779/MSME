package com.metaverse.msme.activity;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class ActivityLogAspect implements Ordered {

    private final ActivityLogService activityLogService;
    private final ObjectMapper mapper;

    public ActivityLogAspect(ActivityLogService activityLogService, ObjectMapper mapper) {
        this.activityLogService = activityLogService;
        this.mapper = mapper;
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void anyRequestMapping() {}

    @Around("anyRequestMapping()")
    public Object logActivity(ProceedingJoinPoint pjp) throws Throwable {

        HttpServletRequest request = currentRequest();
        String actionDescription = describe(request, pjp);
        String actionType = request != null ? request.getMethod() : "UNKNOWN";
        String requestPayload = requestPayload(pjp);

        try {
            Object result = pjp.proceed();
            activityLogService.logSuccess(
                    actionType, actionDescription,
                    requestPayload, request);
            return result;
        } catch (Throwable ex) {
            activityLogService.logFailure(
                    actionType, actionDescription,
                    requestPayload, ex.getMessage(),
                    request);
            throw ex;
        }
    }

    private HttpServletRequest currentRequest() {
        var attrs = RequestContextHolder.getRequestAttributes();
        if (attrs instanceof ServletRequestAttributes sra) {
            return sra.getRequest();
        }
        return null;
    }

    private String describe(HttpServletRequest request, ProceedingJoinPoint pjp) {
        if (request != null) {
            return request.getMethod() + " " + request.getRequestURI();
        }
        return pjp.getSignature().toShortString();
    }

    private String requestPayload(ProceedingJoinPoint pjp) {
        Object[] args = pjp.getArgs();
        if (args == null || args.length == 0) {
            return null;
        }

        try {
            return mapper.writeValueAsString(
                    Arrays.stream(args)
                            .filter(o -> !(o instanceof HttpServletRequest))
                            .toArray());
        } catch (Exception ignored) {
            return Arrays.toString(args);
        }
    }

    @Override
    public int getOrder() {
        // Run late to ensure core logic executes first.
        return Ordered.LOWEST_PRECEDENCE;
    }
}
