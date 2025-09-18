package com.mesprojets.gc.audit;

import javax.sql.DataSource;
import java.sql.*;
import jakarta.servlet.http.HttpServletRequest;

public class ActionLogService {
  private final DataSource ds;

  public ActionLogService(DataSource ds) {
    this.ds = ds;
  }

  public void log(Long userId, String action, String target, String status, HttpServletRequest req) {
    String ip = req != null ? req.getRemoteAddr() : null;
    String ua = req != null ? req.getHeader("User-Agent") : null;
    try (Connection con = ds.getConnection();
         PreparedStatement ps = con.prepareStatement(
           "INSERT INTO action_logs(user_id, action, target, ip, user_agent, status) VALUES (?,?,?,?,?,?)")) {
      if (userId == null) ps.setNull(1, Types.BIGINT); else ps.setLong(1, userId);
      ps.setString(2, action);
      ps.setString(3, target);
      ps.setString(4, ip);
      ps.setString(5, ua);
      ps.setString(6, status != null ? status : "OK");
      ps.executeUpdate();
    } catch (SQLException e) {
      // on évite de casser la requête applicative pour un log
      System.err.println("[action_logs] " + e.getMessage());
    }
  }

  public Long findUserIdByEmail(String email) {
    try (Connection con = ds.getConnection();
         PreparedStatement ps = con.prepareStatement("SELECT id FROM users WHERE email=?")) {
      ps.setString(1, email);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) return rs.getLong(1);
      }
    } catch (SQLException ignored) {}
    return null;
  }
}
