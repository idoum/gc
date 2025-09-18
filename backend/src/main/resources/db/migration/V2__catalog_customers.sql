-- V2: Catalogue & Clients (MySQL 8, utf8mb4)
CREATE TABLE IF NOT EXISTS categories(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(100) NOT NULL UNIQUE,
  name VARCHAR(190) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS products(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  sku VARCHAR(100) NOT NULL UNIQUE,
  name VARCHAR(190) NOT NULL,
  description TEXT NULL,
  unit_price DECIMAL(14,2) NOT NULL,
  currency VARCHAR(10) NOT NULL DEFAULT 'CFA',
  tax_rate DECIMAL(5,2) NOT NULL DEFAULT 0.00, -- ex 18.00 pour 18%
  active BOOLEAN NOT NULL DEFAULT TRUE,
  category_id BIGINT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_products_category FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS customers(
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(100) NOT NULL UNIQUE,
  name VARCHAR(190) NOT NULL,
  email VARCHAR(190) NULL,
  phone VARCHAR(60) NULL,
  address VARCHAR(255) NULL,
  country VARCHAR(100) NULL,
  tax_id VARCHAR(100) NULL,      -- identifiant fiscal si besoin
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Seed minimum
INSERT INTO categories(code,name) VALUES
('general','Général'), ('services','Services')
ON DUPLICATE KEY UPDATE name=VALUES(name);

INSERT INTO products(sku,name,unit_price,currency,tax_rate,active,category_id)
SELECT 'SKU-001','Article démo', 1000.00,'CFA', 18.00, TRUE, c.id
FROM categories c WHERE c.code='general'
ON DUPLICATE KEY UPDATE name=VALUES(name), unit_price=VALUES(unit_price);

INSERT INTO customers(code,name,email,phone,country)
VALUES ('CUST-001','Client Démo','demo@example.com','+225 01 02 03 04','CI')
ON DUPLICATE KEY UPDATE name=VALUES(name);