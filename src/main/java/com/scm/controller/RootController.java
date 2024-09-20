package com.scm.controller;

import com.scm.helper.Helper;
import com.scm.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class RootController {
    @Autowired
    private UserService userService;

    @ModelAttribute
    public void loggedInUserInfo(Model model, Authentication authentication) {
        if(authentication==null){
            return;
        }
        String userName = Helper.getEmailFromPrincipal(authentication);

         model.addAttribute("loggedInUser", userService.getUserByEmail(userName));
    }
}
