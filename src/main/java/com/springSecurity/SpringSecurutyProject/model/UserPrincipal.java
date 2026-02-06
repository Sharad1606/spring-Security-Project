package com.springSecurity.SpringSecurutyProject.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserPrincipal implements UserDetails {

//    private String username;
//    private int password;
//    public UserPrincipal(Users user) {
//        this.username = user.getUsername();
//        this.password = user.getPassword();
//    }

    private Users user;
    public UserPrincipal(Users user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return null;

        // as this requires collection of object and we want to retrun single obj we can use singleton function
        return Collections.singleton(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getPassword() {
//        System.out.println(this.user.getPassword());
       return this.user.getPassword();
    }

    @Override
    public String getUsername() {
//        System.out.println(this.user.getUsername());
        return this.user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
//        return UserDetails.super.isAccountNonExpired();
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
//        return UserDetails.super.isAccountNonLocked();
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
//        return UserDetails.super.isCredentialsNonExpired();
        return true;
    }

    @Override
    public boolean isEnabled() {
//        return UserDetails.super.isEnabled();
        return true;
    }

}
