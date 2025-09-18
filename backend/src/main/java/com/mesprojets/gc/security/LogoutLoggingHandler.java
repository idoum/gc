package com.mesprojets.gc.security;

import com.mesprojets.gc.audit.ActionLogService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import java.io.IOException;

public class LogoutLoggingHandler implements LogoutSuccessHandler {
  private final ActionLogService logs;

  public LogoutLoggingHandler(ActionLogService logs) {
    this.logs = logs;
  }

  @Override
  public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {
    String email = authentication != null ? authentication.getName() : null;
    Long uid = email != null ? logs.findUserIdByEmail(email) : null;
    logs.log(uid, "auth.logout", null, "OK", request);
    response.sendRedirect("/login?logout");
  }
}