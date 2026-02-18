package com.janani.contentrecommendation.config;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.*;
/*
This class is a utility helper for Jwt Operations
This class will create,validate and extract info from the token for authorization and authentication
 */
//@Component Tells the spring to treat this class a Bean  so It can be injected anywhere it needed by using @Autowired
@Component
public class JwtUtil {
    private final Key key;
    //Secret key used to sign and validate JWT
    private final long expiration;
    //Token expiry time (in milliseconds)
    //Comes from application.properties
    //@Value("${jwt.secret}"),@Value("${jwt.expiration}")  read values from application.properties
    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.expiration}") long expiration) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());//Converts the secret string into cryptographic key-HS256 algorithm
        this.expiration = expiration;
    }
    // Generate JWT token with role claim
    //This method creates and returns JWT Token
    public String generateToken(String email, String role) {
        Map<String, Object> claims = new HashMap<>();
        //A claim is data stored inside JWT payload.
        claims.put("role", role);//adding a custom claim in jwt token
        // A claim is piece of info stored in the payload.
        return Jwts.builder()//Used to build the builder pattern
                .setClaims(claims)//adds role claim.
                .setSubject(email)//sets username/email as subject.
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS256)//Creates the signature part of the token
                .compact();// Converts header,payload,signature into jwt string
    }
    //  Extract username (subject)
    public String extractUsername(String token)
    {
        return extractClaims(token).getSubject();
    }
    // Validate token: signature + expiration
    public boolean validateToken(String token) {
        try {
            Claims claims = extractClaims(token);

            // Explicit expiration check
            if (claims.getExpiration().before(new Date())) {
                return false;
            }

            // Optional: check subject is not null
            return claims.getSubject() != null;
        } catch (JwtException | IllegalArgumentException e) {
            return false; // invalid signature, malformed token, etc.
        }
    }

    // Extract the role from the JWT and convert it into a Spring Security Authority object.
    public Collection<SimpleGrantedAuthority> getAuthorities(String token) {
        Claims claims = extractClaims(token);//This verifies the token’s signature using the secret key.If valid, it returns the payload (the data inside the JWT).

        //SimpleGrantedAuthority is a wrapper object that represents a single permission/role.
        String role = claims.get("role", String.class);//get the custom role claim we added during the token creation
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
        //singletonList- A list containing exactly ONE element.
    }

    // It verifies the JWT using the secret key and then extracts the payload (claims).
    //core verification process
    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()//This creates a JWT parser builder.-I want to create a tool that can read and verify JWT tokens.
                .setSigningKey(key)//This sets the secret key used for signature verification.
                .build()/*
                This builds the parser with the configuration.Now it is ready to:Verify signature and Decode token
                */
                .parseClaimsJws(token)//It splits the token into 3 parts header,payload and signature
                .getBody();//It returns the payload
    }
}
