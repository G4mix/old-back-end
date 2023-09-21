package com.gamix.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.gamix.security.JwtAuthenticationProvider;
import com.gamix.security.JwtAuthenticationTokenFilter;
import com.gamix.security.JwtSuccessHandler;

@EnableMethodSecurity(prePostEnabled = true)
@Configuration
public class JwtSecurityConfig  {
    @Autowired
    private JwtAuthenticationProvider authenticationProvider;

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Collections.singletonList(authenticationProvider));
    }

    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilter() {
        JwtAuthenticationTokenFilter filter = new JwtAuthenticationTokenFilter();
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(new JwtSuccessHandler());
        return filter;
    }

    protected void configure(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(
                (authorize) -> authorize
                    .requestMatchers(HttpMethod.POST, "/auth/signup", "/auth/signin").anonymous()
                    .requestMatchers(HttpMethod.GET, "/auth/signout").hasAuthority("USER")
                    .requestMatchers(HttpMethod.POST, "/graphql").hasAuthority("USER")
                    .anyRequest().denyAll()
            )
            .sessionManagement(
                (sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

        http.addFilterBefore(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        http.headers((header) -> header.cacheControl(Customizer.withDefaults()));
    }
}