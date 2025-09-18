package com.mesprojets.gc.security;

import com.mesprojets.gc.audit.ActionLogService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class SecurityEvents {
  private final ActionLogService logs;

  public SecurityEvents(ActionLogService logs) {
    this.logs = logs;
  }

  private HttpServletRequest req() {
    var attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    return attrs != null ? attrs.getRequest() : null;
  }

  @EventListener
  public void onAuthSuccess(AuthenticationSuccessEvent ev) {
    Authentication auth = ev.getAuthentication();
    String email = auth.getName();
    Long uid = logs.findUserIdByEmail(email);
    logs.log(uid, "auth.login.success", null, "OK", req());
  }

  @EventListener
  public void onAuthFailure(AbstractAuthenticationFailureEvent ev) {
    String email = String.valueOf(ev.getAuthentication().getPrincipal());
    Long uid = logs.findUserIdByEmail(email);
    logs.log(uid, "auth.login.failure", null, "KO", req());
  }
}