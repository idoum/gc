package com.mesprojets.gc.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
public class I18nConfig implements WebMvcConfigurer {

  @Bean
  public MessageSource messageSource() {
    ResourceBundleMessageSource ms = new ResourceBundleMessageSource();
    ms.setBasename("messages");
    ms.setDefaultEncoding("UTF-8");
    ms.setFallbackToSystemLocale(false);
    return ms;
  }

  @Bean
  public LocaleResolver localeResolver() {
    SessionLocaleResolver slr = new SessionLocaleResolver();
    slr.setDefaultLocale(Locale.FRENCH);
    return slr;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new HandlerInterceptor() {
      @Override
      public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Déduire la langue du premier segment: /fr/... ou /en/...
        String uri = request.getRequestURI(); // ex: /fr, /en/produits
        String lang = null;
        if (uri.length() >= 3 && uri.charAt(0) == '/' ) {
          String seg = uri.substring(1, Math.min(3, uri.length())); // "fr" ou "en"
          if ("fr".equals(seg) || "en".equals(seg)) {
            lang = seg;
          }
        }
        Locale locale = "en".equals(lang) ? Locale.ENGLISH : Locale.FRENCH;
        request.setAttribute("lang", lang != null ? lang : "fr");
        // place la locale dans le resolver de session
        localeResolver().setLocale(request, response, locale);
        return true;
      }
    });
  }

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    // / -> /fr
    registry.addRedirectViewController("/", "/fr");
  }
}