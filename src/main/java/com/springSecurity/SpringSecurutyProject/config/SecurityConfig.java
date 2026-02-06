package com.springSecurity.SpringSecurutyProject.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


//customize the default filterChain of spring security
@Configuration
@EnableWebSecurity // don't go with the default filterChain flow but the below one I made here
public class SecurityConfig {


    @Autowired
    private JWTFilter jwtFilter;

    @Bean // Managed by spring boot only
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // Customizer is itself an interface

        //imperative way to diable the csrf
        // Customizer<CsrfConfigurer<HttpSecurity>> csrfCustomizer = new Customizer<CsrfConfigurer<HttpSecurity>>() {
        //  @Override
        // public void customize(CsrfConfigurer<HttpSecurity> customizer) {
        // customizer.disable();
        //        }
        //  };
        // http.csrf(csrfCustomizer);
        //lambda way:   http.csrf(customizer->customizer.disable());





        // return the filterchain using HttpSecurity object's methods with no filter applied
        // removed the security login-form as well.
        // return http.build();

        //disable csrf => initial days: http.csrf.disable();
//        1st:
//        http.csrf(customizer->customizer.disable());

        // as csrf is disabled and also no login form is there so
        // enable authorize who are authenticated
        // gives localhost access denied =>as no ways to use pass cred - user/pass sent by client
//       2nd
//        http.authorizeHttpRequests(request->request.anyRequest().authenticated());

        //Enabling form-login => form in browser but "login form-code" in postman
//        3rd
//        http.formLogin(Customizer.withDefaults());
        //Enable for postman

//        4th
//          http.httpBasic(Customizer.withDefaults());

         // make HTTP stateless => send every request with new sessionId
        // postman work fine as it doesn't have form login page cred in basic-auth
        // At browser every login gives new SessionId so login-form re-opens (no cookie)
        // so disable above form-login as HTTP basic gives pop-up for cred itself
//       5th
//        http.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//        return http.build();


        //As these are methods are following builder patter let's convert them in so
        // one object passing throw chain of methods here:
       return http
                .csrf(customizer->customizer.disable())
               // asking for auth to any request comes to server but for login->(we will verify it own) and registration it should not be there
               // so do open some as open-resource and close others
               // so there are some link on which we can avoid applying authentication
//              .authorizeHttpRequests(request->request.anyRequest().authenticated())
               .authorizeHttpRequests(request->request
                       .requestMatchers("/login","/register")
                       .permitAll()// above patterns are permitted/ open links and rest are checked/closed resources
                       .anyRequest().authenticated())

//                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
               . addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }

    // not a suggested way as we are hardcoding here in code it should come from database only
//    @Bean
//    public UserDetailsService userDetailsService(){
//        //defining own authentication
//        UserDetails user1 = User
//                .withDefaultPasswordEncoder()
//                .username("Aman")
//                .password("A@123")
//                .roles("USER")
//                .build(); // build this particular obj
//
//        UserDetails user2 = User
//                .withDefaultPasswordEncoder()
//                .username("Harsh")
//                .password("H@123")
//                .roles("ADMIN")
//                .build(); // build this particular obj
//        return new InMemoryUserDetailsManager(user1,user2); // disabled default authentication as returning our obj accepts varArgs
//    }

 // Create bean to change Auth provider itself from UserDetailsService to some DB one
 //AuthenticationProvider -> interface needed object to use => define or use any class having implemented.


    // As this is implemented as default from prop file and container will throw its obj
    // but if we create own definition obj via class implementation of this interface :)
    @Autowired
    UserDetailsService userDetailsService;

    // we are authenticating here the user/s who are trying to access as a basic auth:
    @Bean
    public AuthenticationProvider authenticationProvider (){
        // impements AutheicationProvider so return the object for DB connection
        // but we don't have definition to connect and get data.
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        // not using any pass encoder by noops instance
//        provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());

        //using bcrypt for authentication with the same strength used in encoding
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));


        // set our own userDetailsService not default one
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }


    // Need obj of AuthManager to hold on it which talks to authentication Provider as per security flow,
    //  but it is interface,
    // we have obj of AuthenticationConfig obj's method to do so
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
           return config.getAuthenticationManager(); // we need to get hold on this obj
    }

}
