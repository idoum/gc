package com.mesprojets.gc.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

public class DbUserDetailsService implements UserDetailsService {
  private final DataSource dataSource;

  public DbUserDetailsService(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // username = email
    String sqlUser = "SELECT id, email, password_hash, is_active FROM users WHERE email = ?";
    String sqlRoles = "SELECT UPPER(REPLACE(r.code,'role.','ROLE_')) AS role_name " +
                      "FROM roles r JOIN user_roles ur ON r.id = ur.role_id " +
                      "JOIN users u ON u.id = ur.user_id WHERE u.email = ?";

    try (Connection con = dataSource.getConnection();
         PreparedStatement ps = con.prepareStatement(sqlUser)) {
      ps.setString(1, username);
      try (ResultSet rs = ps.executeQuery()) {
        if (!rs.next()) {
          throw new UsernameNotFoundException("Utilisateur introuvable: " + username);
        }
        long userId = rs.getLong("id");
        String email = rs.getString("email");
        String pwd = rs.getString("password_hash");
        boolean enabled = rs.getBoolean("is_active");

        // Rôles
        List<GrantedAuthority> auths = new ArrayList<>();
        try (PreparedStatement ps2 = con.prepareStatement(sqlRoles)) {
          ps2.setString(1, email);
          try (ResultSet rr = ps2.executeQuery()) {
            while (rr.next()) {
              auths.add(new SimpleGrantedAuthority(rr.getString("role_name")));
            }
          }
        }
        return User.withUsername(email)
            .password(pwd)
            .authorities(auths)
            .accountLocked(!enabled)
            .disabled(!enabled)
            .build();
      }
    } catch (SQLException e) {
      throw new UsernameNotFoundException("Erreur DB: " + e.getMessage(), e);
    }
  }
}