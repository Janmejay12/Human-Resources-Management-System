package com.example.HRMS.config;

import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.util.List;

@Configuration
public class OpenAPIConfig {
    @Value("${swagger-server-description}")
    private String swaggerServerDescription;

    @Value("${swagger-server-url}")
    private String swaggerServerUrl;

    @Value("${swagger-info-title}")
    private String swaggerInfoTitle;

    @Value("${swagger-info-version}")
    private String swaggerInfoVersion;

    @Value("${swagger-info-description}")
    private String swaggerInfoDescription;

    @Bean
    public OpenAPI customOpenAPI(){

        Server server = new Server();
        server.setUrl(swaggerServerUrl);
        server.setDescription(swaggerServerDescription);

        Contact contact = new Contact();


        Info info = new Info()
                .title(swaggerInfoTitle)
                .version(swaggerInfoVersion)
                .contact(contact)
                .description(swaggerInfoDescription);

        return new OpenAPI().info(info).servers(List.of(server));
    }
}
