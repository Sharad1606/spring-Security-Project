package com.springSecurity.SpringSecurutyProject.controller;

import com.springSecurity.SpringSecurutyProject.model.Student;
import com.springSecurity.SpringSecurutyProject.model.Users;
import com.springSecurity.SpringSecurutyProject.repo.UsersRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class StudentController {

    private List<Student> studentList =  new ArrayList<>(Arrays.asList(
           new Student(1,"sharad",100),
           new Student(2,"aman",50),
           new Student(3,"soham",60)

   ));

    @Autowired
    UsersRepo usersRepo;

    @GetMapping("/data")
     public  Users getData(){
        System.out.println(this.usersRepo.findByUsername("Sharad"));
        return new Users();
    }


    @GetMapping("/students")
   ResponseEntity<List<Student>> getAllStudents(){
        return new ResponseEntity<>(studentList, HttpStatus.OK);
    }


    @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest request){
        //getAttribute value having parameterName as _csrf.
        return (CsrfToken) request.getAttribute("_csrf");
    }

    // from client like postman first request for csrf token with headerName X-CSRF-TOKEN
    // then use in header for post,put,delete
    @PostMapping("/students")
    public ResponseEntity<Student> addStudent(@RequestBody Student student){
        this.studentList.add(student);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

}
