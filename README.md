@'
# Gestion Commerciale — Monolithe (Spring Boot + MySQL)

- Back: Spring Boot 3, Thymeleaf, Spring Security, Data JPA, Flyway
- DB: MySQL 8
- UI: Bootstrap 5.3 (SSR) + HTMX/Alpine
- i18n: FR/EN, URLs /fr et /en
- Thème: clair/sombre (data-bs-theme)
- Déploiement: NGINX (TLS) → service Java, MySQL managé ou VPS

## Démarrer en local
Voir: ops/docker/docker-compose.dev.yml et .env
'@ | Set-Content -Encoding UTF8 README.md
