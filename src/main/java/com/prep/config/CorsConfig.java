package com.prep.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
           
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(
                                // 🔹 Local
                                "http://localhost:5500",
                                "http://localhost:5501",
                                "http://localhost:5502",
                                "http://127.0.0.1:5500",
                                "http://127.0.0.1:5501",
                                "http://127.0.0.1:5502",

                                // 🔹 Netlify Frontend (USER)
                                "https://prepyan-app.netlify.app",

                                // 🔹 Netlify Admin
                                "https://prepyan-admin.netlify.app"
                        )
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}