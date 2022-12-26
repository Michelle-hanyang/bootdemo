package com.example.bootdemo.config;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2WebMvc
public class SwaggerConfig {

    /**
     * 后台管理系统接口-应用维护子系统
     *
     * @return
     */
    @Bean
    public Docket gvmApi() {

        ParameterBuilder ticketPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        ticketPar.name("token")
                .description("鉴权token")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(true)
                .build();
        pars.add(ticketPar.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo(
                        "后台管理系统接口-应用维护子系统",
                        "后台管理系统接口-应用维护子系统",
                        "lsm",
                        "1.0"
                ))
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build()
                .groupName("后台管理系统接口-应用维护子系统")
                .pathMapping("/")
                .globalOperationParameters(pars);
    }


    /**
     * 接口组基础信息通用设置模板
     *
     * @param title       接口组标题
     * @param description 接口组描述
     * @param contact     接口组作者
     * @param version     接口组版本
     * @return
     */
    private ApiInfo apiInfo(String title, String description, String contact, String version) {
        return new ApiInfoBuilder().title(title).description(description).contact(contact).version(version).build();
    }

}
