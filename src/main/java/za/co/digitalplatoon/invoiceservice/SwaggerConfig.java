package za.co.digitalplatoon.invoiceservice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationCodeGrant;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.OAuth;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.Tag;
import springfox.documentation.service.TokenEndpoint;
import springfox.documentation.service.TokenRequestEndpoint;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

// Spring annotations
@Configuration
// Swagger annotations
@EnableSwagger2
@Import({BeanValidatorPluginsConfiguration.class})
public class SwaggerConfig {

    public static final String O_AUTH_2 = "OAuth2";

    @Value("${project.version}")
    private String projectVersion;

    @Value("${keycloak.auth-server-url}")
    private String keycloakAuthServerUrl;

    @Value("${keycloak.realm}")
    private String keycloakRealm;

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
                )
                .securitySchemes(securitySchema())
                .securityContexts(securityContext());
    }

    private List<OAuth> securitySchema() {
        String keycloakRealmUrl = keycloakAuthServerUrl + "/realms/" + keycloakRealm + "/protocol/openid-connect/";
        TokenRequestEndpoint tokenRequestEndpoint = new TokenRequestEndpoint(keycloakRealmUrl + "auth", "swagger-ui", null);
        TokenEndpoint tokenEndpoint = new TokenEndpoint(keycloakRealmUrl + "token", "access_token");
        AuthorizationCodeGrant authorizationCodeGrant = new AuthorizationCodeGrant(tokenRequestEndpoint, tokenEndpoint);
        return Arrays.asList(new OAuth(O_AUTH_2, Arrays.asList(), Arrays.asList(authorizationCodeGrant)));
    }

    private List<SecurityContext> securityContext() {
        SecurityContext securityContext = SecurityContext
                .builder()
                .securityReferences(defaultAuth())
                .build();
        return Arrays.asList(securityContext);
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[0];
        return Arrays.asList(new SecurityReference(O_AUTH_2, authorizationScopes));
    }

    /**
     * A bean to configure the values that will be returned at the
     * /swagger-resources/configuration/security rest endpoint to configure the
     * Swagger UI frontend of SpringFox
     *
     * @return The security configuration for swagger UI
     */
    @Bean
    SecurityConfiguration security() {
        return SecurityConfigurationBuilder
                .builder()
                .realm(keycloakRealm)
                .clientId("swagger-ui")
                .appName("invoice-service")
                .build();
    }

}
