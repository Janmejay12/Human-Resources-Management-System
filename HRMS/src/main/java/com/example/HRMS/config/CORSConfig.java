package com.example.HRMS.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CORSConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**") //add your api endpoints here
                .allowedOrigins("http://localhost:5173") //add your origin URL here
                .allowedMethods("GET", "POST","PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true).maxAge(3600);

    }
}
