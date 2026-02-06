package com.springSecurity.SpringSecurutyProject.service;

import com.springSecurity.SpringSecurutyProject.model.Users;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import io.jsonwebtoken.Claims;
@Service
public class JWTService {


// The specified key byte array is 40 bits which is not secure enough
// for any JWT HMAC-SHA algorithm.  The JWT JWA Specification
// (RFC 7518, Section 3.2) states that keys used with HMAC-SHA algorithms MUST have a size >= 256 bits
// Not safe so create it
//    String secretKey = "@134dhjd";

// As it is required to be new of each user so use constructor

    String secretKey = "";

    public JWTService(){
        try {
            KeyGenerator key = KeyGenerator.getInstance("HmacSHA256");
            SecretKey sk = key.generateKey(); // return byte[]
            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded()); // convert back to string and assign
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    public String getToken(String username) {

       // key - string, value - varient types so used obj
        Map<String, Object> claims = new HashMap<>();

        // to craete token JWTS  provide support and we have to do multi steps so can use its builder method
        return Jwts
                .builder()
                .setClaims(claims)
                .addClaims(claims)
                .setSubject(username)   // subject is username in jwt
                .setIssuedAt(new Date(System.currentTimeMillis()))  // set current system time using date obj
                .setExpiration(new Date(System.currentTimeMillis() + (60*60*30) )) // applicable till 30 mins
                .signWith(getKey())  // generate a key here  => lets create somewhere else
                .compact(); // it will generate token for username and password you provide at login and can be verified on internet.
//        return "Hehe token";
    }

    // return a random key for JWT:

    public Key getKey() {
        // convert the string to byte []
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        System.out.println("key for JWT: "+keyBytes);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserName(String token) {
        // extract the username from jwt token
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}


// need to create and return JWT token
        /*
        header:
            {
            "alg": "HS256",
            "typ": "JWT"
            }
        body: to impl these fields we need to create a map:
            {
            "sub": "1234567890",
            "name": "John Doe",
            "admin": true,
            "iat": 1516239022
            }

        Signature: optional
        a-string-secret-at-least-256-bits-long



         */