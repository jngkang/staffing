package com.staffing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableOpenApi
@EnableWebMvc
public class SwaggerConfig {

//    @Bean
//    public Docket restApi() {
//        return new Docket(DocumentationType.OAS_30)//2.0版本的为SWAGGER_2
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.xxx.controller"))
//                .build();
//    }

    @Bean
    public Docket restApi() {
        return new Docket(DocumentationType.OAS_30)
                .groupName("标准接口")
                .apiInfo(apiInfo("SpringbootVueDemo", "1.0"))
                .useDefaultResponseMessages(true)
                .forCodeGeneration(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.xxx.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo(String title, String version) {
        return new ApiInfoBuilder()
                .title(title)
                .version(version)
                .build();
    }

}