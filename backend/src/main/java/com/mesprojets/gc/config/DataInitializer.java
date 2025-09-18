package com.mesprojets.gc.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.sql.*;

@Configuration
public class DataInitializer {

  @Bean
  ApplicationRunner seedAdmin(DataSource ds, PasswordEncoder encoder) {
    return args -> {
      String adminEmail = System.getenv().getOrDefault("ADMIN_EMAIL", "admin@example.com");
      String adminPass  = System.getenv().getOrDefault("ADMIN_PASSWORD", "Admin@123");

      try (Connection con = ds.getConnection()) {
        con.setAutoCommit(false);
        try {
          // S'assurer que le rôle 'role.admin' existe
          try (PreparedStatement ps = con.prepareStatement(
              "INSERT INTO roles(code, label) SELECT 'role.admin','Administrateur' " +
              "WHERE NOT EXISTS (SELECT 1 FROM roles WHERE code='role.admin')")) {
            ps.executeUpdate();
          }
          // Vérifier si l'admin existe
          Long userId = null;
          try (PreparedStatement ps = con.prepareStatement(
              "SELECT id FROM users WHERE email = ?")) {
            ps.setString(1, adminEmail);
            try (ResultSet rs = ps.executeQuery()) {
              if (rs.next()) userId = rs.getLong(1);
            }
          }
          if (userId == null) {
            // Créer l'admin
            String hash = encoder.encode(adminPass);
            try (PreparedStatement ps = con.prepareStatement(
                "INSERT INTO users(email, password_hash, full_name, is_active, locale, theme) " +
                "VALUES(?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
              ps.setString(1, adminEmail);
              ps.setString(2, hash);
              ps.setString(3, "Administrateur");
              ps.setBoolean(4, true);
              ps.setString(5, "fr");
              ps.setString(6, "light");
              ps.executeUpdate();
              try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) userId = rs.getLong(1);
              }
            }
            // Lier le rôle admin
            try (PreparedStatement ps = con.prepareStatement(
                "INSERT INTO user_roles(user_id, role_id) " +
                "SELECT ?, r.id FROM roles r WHERE r.code='role.admin'")) {
              ps.setLong(1, userId);
              ps.executeUpdate();
            }
          }
          con.commit();
        } catch (Exception ex) {
          con.rollback();
          throw ex;
        } finally {
          con.setAutoCommit(true);
        }
      }
    };
  }
}