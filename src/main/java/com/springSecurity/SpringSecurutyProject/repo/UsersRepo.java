package com.springSecurity.SpringSecurutyProject.repo;

import com.springSecurity.SpringSecurutyProject.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// no suggestion for JPARepository => not added dependency
@Repository
public interface UsersRepo extends JpaRepository<Users, Integer> {

    Users findByUsername(String username);



}
