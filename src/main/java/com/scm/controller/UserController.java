package com.scm.controller;


import com.scm.entities.User;
import com.scm.helper.Helper;
import com.scm.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
/**
 * Handles the GET request for the "/dashboard" URL and returns the "user/dashboard" view.
 *
 * @return  the name of the view to render
 */

// Protected URLS
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;



    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String dashboard() {
        return "user/dashboard";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String profile(Model model,Authentication authentication) {
        return "user/profile";
    }
}
