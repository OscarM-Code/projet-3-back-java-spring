package com.projetjavaopc.api.swagger;

import java.util.ArrayList;
import java.util.List;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.DocExpansion;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    @Primary
public SwaggerResourcesProvider swaggerResourcesProvider() {
    return () -> {
        List<SwaggerResource> resources = new ArrayList<>();
        resources.add(swaggerResource("my-service", "/v2/api-docs", "3.0"));
        //ajouter d'autres resources swagger si besoin
        return resources;
    };
}

private SwaggerResource swaggerResource(String name, String location, String version) {
    SwaggerResource swaggerResource = new SwaggerResource();
    swaggerResource.setName(name);
    swaggerResource.setLocation(location);
    swaggerResource.setSwaggerVersion(version);
    return swaggerResource;
}

    @Bean
    public UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder()
                .docExpansion(DocExpansion.LIST)
                .build();
    }

    @Bean
    public Docket api() {
      return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.projetjavaopc.api.controller"))
        .paths(PathSelectors.any())
        .build()
        .apiInfo(apiInfo());
    }
  
    private ApiInfo apiInfo() {
      return new ApiInfoBuilder()
        .title("Mon API REST")
        .description("Description de mon API REST")
        .contact(new Contact("Millot et Oscar", "https://monsite.com", "o.millot@domaine.com"))
        .version("1.0")
        .build();
    }

}