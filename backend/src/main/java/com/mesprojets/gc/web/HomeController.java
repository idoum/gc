package com.mesprojets.gc.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {

  @GetMapping({"/{lang:fr|en}"})
  public String home(@PathVariable String lang) {
    return "index";
  }

  @GetMapping("/login")
  public String login() { return "login"; }
}