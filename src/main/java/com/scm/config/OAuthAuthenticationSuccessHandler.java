package com.scm.config;


import com.scm.entities.Providers;
import com.scm.entities.User;
import com.scm.helper.AppConstants;
import com.scm.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    Logger logger = LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);

    @Autowired
    private UserRepository userRepository;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

          /* DefaultOAuth2User user = (DefaultOAuth2User) authentication.getPrincipal();
        logger.info(user.getName());

        user.getAttributes().forEach((key, value) -> {
            logger.info(key + " " + value);
        });
        logger.info(user.getAuthorities().toString());
        String email = user.getAttributes().get("email").toString();
        String name = user.getAttributes().get("name").toString();
        String picture = user.getAttributes().get("picture").toString();

        // Create user and save in database
        User user1 = new User();

        user1.setName(name);
        user1.setEmail(email);
        user1.setProfilePic(picture);
        user1.setUserId(UUID.randomUUID().toString());
        user1.setEnabled(true);
        user1.setProvider(Providers.GOOGLE);
        user1.setProviderUserId(email);
        user1.setRoleList(List.of(AppConstants.ROLE_USER));
        User user2 = userRepository.findByEmail(email).orElse(null);
        if (user2 == null) {
            userRepository.save(user1);
            logger.info("User Saved");
        }
        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile"); // after oauth2login redirect to user profile */
        /* Identify it is Google, GitHub or facebook */

        var auth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        String authId = auth2AuthenticationToken.getAuthorizedClientRegistrationId(); // to check which third party login

        DefaultOAuth2User authUser = (DefaultOAuth2User) authentication.getPrincipal();
//        authUser.getAttributes().forEach((key, value) -> {
//            logger.info(key + " " + value);
//        }); // Login with personal email
        User user = new User();

        String email = "";
        user.setUserId(UUID.randomUUID().toString());
        user.setRoleList(List.of(AppConstants.ROLE_USER));
        user.setEmailVerified(true);
        user.setEnabled(true);
        // after oauth2login redirect to user profile
        if (authId.equalsIgnoreCase("google")) {
            email = authUser.getAttributes().get("email").toString();
            user.setEmail(email);
            user.setProfilePic(authUser.getAttributes().get("picture").toString());
            user.setName(authUser.getAttributes().get("name").toString());
            user.setProviderUserId(authUser.getName());


        } else if (authId.equalsIgnoreCase("github")) {
            user.setName(authUser.getAttributes().get("login").toString());
            //Setting up email
            email = authUser.getAttribute("email") != null ? authUser.getAttribute("email").toString() : authUser.getAttribute("login").toString();
            String picture = authUser.getAttribute("avatar_url").toString();
            user.setName(authUser.getAttributes().get("login").toString());

            user.setProfilePic(picture);
            user.setEmail(email);
        }

        User user2 = userRepository.findByEmail(user.getEmail()).orElse(null);
        if (user2 == null) {
            userRepository.save(user);
            logger.info("User Saved");
        }
        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");
        //logger.info(authId);
    }
}
