package com.yugal.store.commons;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * CORS for the hosted ORCHARD frontend ONLY.
 * Frontend origin: ${websiteUrl} - no trailing slash, no path, no "*".
 * Never use "*" here: Spring rejects "*" together with
 * allowCredentials=true, and a wildcard with credentials would let any
 * site make credentialed requests to the API.
 */
@Configuration
public class CorsConfig {

    @Value("${websiteUrl}")
    private String frontendOrigin;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        var config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(frontendOrigin));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE","OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);    // 1 hour
        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
