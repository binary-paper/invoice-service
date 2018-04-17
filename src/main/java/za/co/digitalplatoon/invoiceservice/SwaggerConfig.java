package za.co.digitalplatoon.invoiceservice;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

// Spring annotations
@Configuration
// Swagger annotations
@EnableSwagger2
public class SwaggerConfig {

    @Value("${project.version}")
    private String projectVersion;

    @Bean
    public Docket swaggerApi() {
        ApiInfo apiInfo = new ApiInfo(
                "Invoice Service REST API",
                "REST API documentation",
                projectVersion,
                "",
                new Contact("Willy Gadney", "", "gadnex@gmail.com"),
                "Apache License Version 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList<>()
        );
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("za.co.digitalplatoon.invoiceservice"))
                .paths(PathSelectors.regex("/.*"))
                .build()
                .apiInfo(apiInfo)
                .tags(
                        new Tag("Invoice", "Invoice REST resource")
                );
    }

}
