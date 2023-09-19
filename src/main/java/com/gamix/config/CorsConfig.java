package com.gamix.config;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

// import io.github.cdimascio.dotenv.Dotenv;

@Configuration
public class CorsConfig {
    // @Autowired
    // private Dotenv dotenv;
    
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        //config.addAllowedOrigin(dotenv.get("FRONT_END_BASE_URL"));
        config.addAllowedOrigin("*");

        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");

        config.addAllowedHeader(HttpHeaders.AUTHORIZATION);
        config.addAllowedHeader(HttpHeaders.CONTENT_TYPE);

        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
