package com.scm.config;



import com.scm.services.impl.SecurityCustomDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;



@Configuration
public class SecurityConfig {

    @Autowired
    private SecurityCustomDetailService customDetailService;

    @Autowired
    private OAuthAuthenticationSuccessHandler handler;

    @Autowired
    private AuthFailureHandler authFailureHandler;



    // User Create and Login Using Java Code within Memory Service

//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails userDetails = User.withUsername("admin")
//
//                .password("admin")
//                .roles("ADMIN").build();
//
//        return new InMemoryUserDetailsManager(userDetails);
//    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        //User Details Service Object
        daoAuthenticationProvider.setUserDetailsService(customDetailService);
        // Password Encoder
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());


        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // configuration
        httpSecurity.authorizeHttpRequests(request->{
           // request.requestMatchers("/home","/register","/login").permitAll(); // It will allow all user to access the link without any password
            request.requestMatchers("/user/**").authenticated();
            request.anyRequest().permitAll();
        }); // link with user tag is authenticated rest is public

        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.formLogin(formLogin->{
            formLogin.loginPage("/login");
            formLogin.loginProcessingUrl("/authenticate");
            formLogin.successForwardUrl("/user/profile");
            formLogin.failureForwardUrl("/login?error=true");
            formLogin.defaultSuccessUrl("/user/dashboard");
            formLogin.usernameParameter("email");
            formLogin.passwordParameter("password");
            formLogin.failureHandler(authFailureHandler);


        });

        httpSecurity.logout(logout->{
            logout.logoutUrl("/logout");
            logout.logoutSuccessUrl("/login?logout=true");
        });
        //oauth configuration
        httpSecurity.oauth2Login(oauth->{
            oauth.loginPage("/login"); // it will show the page we designed for login
            oauth.successHandler(handler); // for handling the user login
        });


        return httpSecurity.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();

    }
}
