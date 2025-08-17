package org.newnop.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI bookRentalAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Book Rental Management System API")
                        .description("RESTful APIs for managing book rentals in a community library system")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Newnop Development Team")
                                .email("team@newnop.com")
                                .url("https://newnop.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}