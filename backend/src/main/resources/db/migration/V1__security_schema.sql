-- V1 — Schéma sécurité de base
-- Encodage: UTF-8, MySQL 8 / utf8mb4

CREATE TABLE IF NOT EXISTS users (
  id            BIGINT PRIMARY KEY AUTO_INCREMENT,
  email         VARCHAR(190) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  full_name     VARCHAR(190) NOT NULL,
  is_active     BOOLEAN NOT NULL DEFAULT TRUE,
  locale        VARCHAR(10)  NOT NULL DEFAULT 'fr',
  theme         VARCHAR(10)  NOT NULL DEFAULT 'light',
  created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS roles (
  id        BIGINT PRIMARY KEY AUTO_INCREMENT,
  code      VARCHAR(100) NOT NULL UNIQUE,       -- ex: role.admin
  label     VARCHAR(190) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS permissions (
  id        BIGINT PRIMARY KEY AUTO_INCREMENT,
  code      VARCHAR(150) NOT NULL UNIQUE,       -- ex: invoice:post
  label     VARCHAR(190) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS user_roles (
  user_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  PRIMARY KEY (user_id, role_id),
  CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS role_permissions (
  role_id BIGINT NOT NULL,
  perm_id BIGINT NOT NULL,
  PRIMARY KEY (role_id, perm_id),
  CONSTRAINT fk_role_perms_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
  CONSTRAINT fk_role_perms_perm FOREIGN KEY (perm_id) REFERENCES permissions(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS action_logs (
  id         BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id    BIGINT NULL,
  action     VARCHAR(120) NOT NULL,    -- ex: auth.login.success, product.create
  target     VARCHAR(190) NULL,        -- ex: product:123
  ip         VARCHAR(64)  NULL,
  user_agent VARCHAR(255) NULL,
  status     VARCHAR(20)  NOT NULL DEFAULT 'OK',
  created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_action_logs_user (user_id),
  INDEX idx_action_logs_action (action),
  CONSTRAINT fk_action_logs_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Rôles initiaux
INSERT INTO roles (code, label) VALUES
  ('role.admin','Administrateur'),
  ('role.ventes','Ventes'),
  ('role.stock','Stock'),
  ('role.compta','Comptabilité'),
  ('role.lecture','Lecture')
ON DUPLICATE KEY UPDATE label=VALUES(label);
