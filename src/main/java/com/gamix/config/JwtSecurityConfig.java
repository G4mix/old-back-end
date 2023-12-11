package com.gamix.config;

import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.SessionManagementFilter;
import com.gamix.security.CorsFilter;
import com.gamix.security.JwtAuthenticationProvider;
import com.gamix.security.JwtAuthenticationTokenFilter;
import com.gamix.security.JwtSuccessHandler;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
@Configuration
public class JwtSecurityConfig {
    private final JwtAuthenticationProvider authenticationProvider;

    @Bean
    CorsFilter corsFilter() {
        return new CorsFilter();
    }

    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(
                    (authorize) -> authorize.requestMatchers(HttpMethod.POST, "/graphql")
                            .anonymous().anyRequest().denyAll()
                            .requestMatchers(HttpMethod.POST, "/auth/signin", "/auth/signup",
                                    "/auth/refreshtoken")
                            .permitAll())
            .sessionManagement((sessionManagement) -> sessionManagement
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(authenticationTokenFilter(),
                UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(corsFilter(), SessionManagementFilter.class);
    }

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
}
