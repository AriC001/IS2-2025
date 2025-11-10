package com.example.etemplate.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.security.core.userdetails.User;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("loggedUser")
    public String loggedUser(@AuthenticationPrincipal User user) {
        return user != null ? user.getUsername() : null;
    }
}
