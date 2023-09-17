package com.gamix.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableMethodSecurity(prePostEnabled = true)
@Configuration
public class JwtSecurityConfig  {
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf(Customizer.withDefaults())
            .authorizeHttpRequests(
                (authorize) -> authorize
                    .requestMatchers(HttpMethod.POST, "/registerPasswordUser").anonymous()
                    .requestMatchers("**/graphql/**").authenticated()
            )
            .sessionManagement(
                (sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

        http.headers((header) -> header.cacheControl(Customizer.withDefaults()));
    }
}