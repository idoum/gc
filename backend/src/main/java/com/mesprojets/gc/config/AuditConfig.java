package com.mesprojets.gc.config;

import com.mesprojets.gc.audit.ActionLogService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class AuditConfig {
  @Bean
  public ActionLogService actionLogService(DataSource ds) {
    return new ActionLogService(ds);
  }
}