package com.springSecurity.SpringSecurutyProject.controller;

import com.springSecurity.SpringSecurutyProject.model.Users;
import com.springSecurity.SpringSecurutyProject.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;


// every controller is a servlet itself and all lies in tomcat-(embedded servlet container)
// And so every methods can hold control on the RestController's HttpServeletRequest and Response
@RestController
public class UsersController {

    @Autowired
    private UsersService usersService;

    @GetMapping("/")
    public String greet(HttpServletRequest request, HttpServletResponse response){
        return "Hello from User Controller with session: " + request.getSession().getId();
    }

    @GetMapping("/users")
    public List<Users> getAllUsers(){
        return usersService.getAllUsers();
    }

    @PostMapping("/register")
    public Users register(@RequestBody Users user){
//        System.out.println(user);
       return usersService.registerUser(user);
//        return user;
    }

    @PostMapping("/login")
    public String login(@RequestBody Users user){
//        System.out.println(user);
        // verify the login user is existing or required to do registration
        return usersService.verify(user);
//        return "Success!";
    }
}
