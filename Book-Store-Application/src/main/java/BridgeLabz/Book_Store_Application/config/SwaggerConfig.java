package BridgeLabz.Book_Store_Application.config;


import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI bookstoreAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Bookstore REST API")
                        .description("Bookstore E-Commerce Backend API Documentation")
                        .version("1.0")
                        .contact(
                                new Contact()
                                        .name("Nandha Kumar")
                                        .email("your-email@example.com")
                        )
                )
                .externalDocs(
                        new ExternalDocumentation()
                                .description("Project Documentation")
                                .url("https://github.com/")
                );

    }
}
