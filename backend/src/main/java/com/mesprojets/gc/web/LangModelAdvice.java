package com.mesprojets.gc.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class LangModelAdvice {

  @ModelAttribute("lang")
  public String addLang(HttpServletRequest request) {
    // essaie d’abord un attribut (filtre/Handler qui l’aurait posé),
    // sinon retombe sur "fr".
    Object attr = request != null ? request.getAttribute("lang") : null;
    return attr != null ? attr.toString() : "fr";
  }
}
