package com.springSecurity.SpringSecurutyProject.service;

import com.springSecurity.SpringSecurutyProject.model.UserPrincipal;
import com.springSecurity.SpringSecurutyProject.model.Users;
import com.springSecurity.SpringSecurutyProject.repo.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UsersRepo usersRepo;

    // as we dont have idea from where to load it as per name => it is service-> get data from repo
    // connect it with repo and use the data from there
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // return null;
        // we have to use userName as per function desc:

        // Users has username/pass from DB
        Users user = usersRepo.findByUsername(username);
        if(user == null){
                 System.out.println("Not found");
                throw  new UsernameNotFoundException("Not found");
        }
        System.out.println(user.getUsername() + " "+user.getPassword());

        // return obj type UserDetails => to check the current user from user entity
        // class having implementation of Userdetails or create ur own user principle

        return new UserPrincipal(user);
    }
}
