package com.springSecurity.SpringSecurutyProject.service;

import com.springSecurity.SpringSecurutyProject.model.Users;
import com.springSecurity.SpringSecurutyProject.repo.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersService {

    @Autowired
    private UsersRepo usersRepo;


    // while saving user we should encrypt/ BCrypt the pass using BCrypt library
    // for decrypting while logging back with bcrypted pass we have configured in securityConfig

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);


    public Users registerUser(Users user) {
//        System.out.println(user +" -> "+ user.getUsername()+" -> "+user.getPassword());
        user.setPassword(encoder.encode(user.getPassword()));
       return usersRepo.save(user);
    }

    public List<Users> getAllUsers() {
        return usersRepo.findAll();
    }


    // till now login was happening auto by authentication manager default behavior
    // but now we have hold on it and want to have our impl as created bean for that in config so do that here

    @Autowired
   private AuthenticationManager authManager;

    @Autowired
   private JWTService jwtService;
    public String  verify(Users user) {
        // we have impl for authetication in config already => we are just using it to apply checks
        // pass un-auth get auth obj
//        Required type:
//        org.apache.tomcat.util.net.openssl.ciphers.Authentication
//        Provided:
//        org.springframework.security.core.Authentication
        Authentication authentication =
                // pass username and pass
                authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
        // we don't want success to return but JWT token when authenticated user login
        if(authentication.isAuthenticated()){
//            return "login successfully..";
            return  jwtService.getToken(user.getUsername());
        }

        else
        return  "please do registeration...";

    }
}
