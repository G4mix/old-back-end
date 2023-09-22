package com.gamix.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
            .cors(cors -> cors.configurationSource(corsFilter()))
            .authorizeHttpRequests(
                (authorize) -> authorize
                    .requestMatchers(HttpMethod.POST, "/auth/signup", "/auth/signin", "/auth/signout").anonymous()
                    .requestMatchers(HttpMethod.POST, "/graphql").hasAuthority("USER")
                    .anyRequest().denyAll()
            )
            .sessionManagement(
                (sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

        http.addFilterBefore(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        http.headers((header) -> header.cacheControl(Customizer.withDefaults()));
    }

    @Bean
    public CorsConfigurationSource corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        
        //config.addAllowedOrigin(dotenv.get("FRONT_END_BASE_URL"));
        config.addAllowedOrigin("*");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedHeader(HttpHeaders.AUTHORIZATION);
        config.addAllowedHeader(HttpHeaders.CONTENT_TYPE);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}