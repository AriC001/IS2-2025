package nexora.ejd.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController{

    @GetMapping("/")
    public String index(@AuthenticationPrincipal OAuth2User oauth2User, Model model) {
    if (oauth2User != null) {
        model.addAttribute("name", oauth2User.getAttribute("name"));
        model.addAttribute("email", oauth2User.getAttribute("email"));
        model.addAttribute("localId", oauth2User.getAttribute("localUserId"));
    }
    return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

}
