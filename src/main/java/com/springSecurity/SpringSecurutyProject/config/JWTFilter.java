package com.springSecurity.SpringSecurutyProject.config;

import com.springSecurity.SpringSecurutyProject.service.JWTService;
import com.springSecurity.SpringSecurutyProject.service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
// to make it behave like filter
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    ApplicationContext context;

    // reqObj -> work with data or req  of the user
    // resObj -> add something to the res to the user
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // req header to the servlet has Authorization header with bearer myJWTToken8348934uriejfkdsnf -> we need to extract the token only
        String authHeader = request.getHeader("Authorization"); // its has lot, we need Authorization one
        String token = null;
        String username = null; // req to verify token with username right???

        // lets check if we have any auth in header
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            //Bearer token
            token = authHeader.substring(7);//startWithIndex 7 bearer+1space
            username = jwtService.extractUserName(token);
        }

        // as we got username and token lets validate iff it is not already authenticated
        // we have to authenticate JWT success -> validate token => if valid create authentication obj
        // then pass further for User/pass filter
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){

            // token should be valid and has username that is part of db already
            // If we craete obj of UserDetails here or do direct autowire it will create cyclic dependency
            // so have hold on appContext to resolve it
            // we can get obj from spring container directly using appContext without any issue

//            UserDetails userDetails = context.getBean(UserDetails.class);
            // we have loadUserByUsername in our userDetailService which act with DB so getBean of this class and call it
            UserDetails userDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(username);// gives all user details from db


            // if token is valid gthen we have to call the next filter user/pass filter
            if(jwtService.validateToken(token, userDetails)){
                // auth token k abt token but not about HTTPReq obj
                UsernamePasswordAuthenticationToken authToken
                        // ask for principal, credentials, authority
                      = new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
                // make it know about HTTPReq  request obj
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // as this authToken ready let's add/set it to context so that it should not be null
                // adding token in chain
                SecurityContextHolder.
                        getContext().setAuthentication(authToken);
            }

        }
        // pass it to the filter continue filter done with one go for next
        // pass two obj req and res
        filterChain.doFilter(request,response);


    }
}
