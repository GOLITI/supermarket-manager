package com.supermarket.manager.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI supermarketOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080");
        devServer.setDescription("Serveur de développement");

        Server prodServer = new Server();
        prodServer.setUrl("https://api.supermarket-manager.com");
        prodServer.setDescription("Serveur de production");

        Contact contact = new Contact();
        contact.setEmail("marcgoliti429@gmail.com");
        contact.setName("Marc GOLITI");
        contact.setUrl("https://github.com/GOLITI");

        License mitLicense = new License()
                .name("MIT License")
                .url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("Supermarket Manager API")
                .version("1.0.0")
                .contact(contact)
                .description("API REST complète pour la gestion d'un supermarché moderne type Carrefour. "
                        + "Cette API permet de gérer les stocks, les commandes fournisseurs, les employés, "
                        + "les clients, la fidélité, les caisses, les transactions et le reporting.")
                .termsOfService("https://www.supermarket-manager.com/terms")
                .license(mitLicense);

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer, prodServer));
    }
}

