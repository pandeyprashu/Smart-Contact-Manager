package com.scm.helper;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;


/**
 * Retrieves the email from the principal based on the type of authentication.
 * //@param  authentication   the authentication object
 * return                 the email associated with the principal
 */

public class Helper {

    public static String getEmailFromPrincipal(Authentication authentication) {

        //  Login with Google
        if (authentication instanceof OAuth2AuthenticationToken) {

            var authToken = (OAuth2AuthenticationToken) authentication;
            var clientId = authToken.getAuthorizedClientRegistrationId();

            var oauth2User=(OAuth2User)authentication.getPrincipal();

            String username="";


            if (clientId.equalsIgnoreCase("google")) {

               System.out.println("Helper Login with Google");
                //  Login with Google
                username= oauth2User.getAttribute("email").toString();

            } else if (clientId.equalsIgnoreCase("github")) {
                System.out.println("Login with GitHub");

                //  Login with GitHub
                username= oauth2User.getAttribute("email")!=null?oauth2User.getAttribute("email").toString():oauth2User.getAttribute("login").toString()+"@gmail.com";


            }
            return username;
        } else {
            // Login with personal email
            return authentication.getName();

        }



    }

}
