package com.janani.contentrecommendation.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
//@Configuration → Marks this class as a Spring configuration class.
// It defines beans (like SecurityFilterChain, PasswordEncoder) that Spring will manage.
@EnableMethodSecurity//Enables method level security and without this @PreAuthorize will not work
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }
/*
CSRF protection is useful for stateful apps (with sessions and cookies).
CSRF (Cross-Site Request Forgery) is an attack where a malicious site tricks a logged-in user into performing unwanted actions (like submitting a form).
 */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("contents/uploads/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")      // only admin
                        .requestMatchers("/curator/**").hasRole("CURATOR")  // only curator
                        .requestMatchers("/users/**").hasAnyRole("USER","ADMIN","CURATOR")
                        .anyRequest().authenticated()//everything else requires authentication.
                )
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();//Finalizes the configuration and returns the built SecurityFilterChain.
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

/*
This is the central component that processes authentication requests.

It delegates to configured authentication providers (like DAO authentication with user details service).

Required if you want to manually authenticate users (e.g., during login endpoint).
 */

/*

Intercepts login requests (by default at /login).

Extracts the username and password from the request.

Passes them to the AuthenticationManager.

If authentication succeeds, it creates an Authentication object and stores it in the SecurityContext.
*/
