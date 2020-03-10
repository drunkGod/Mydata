package com.jvxb.manage.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//@Profile({ "dev", "test" })
@Configuration // 标记配置类
@EnableSwagger2 // 开启在线接口文档
public class Swagger2Config {

	@Bean
	public Docket controllerApi() {
		// api
		ApiInfo api = new ApiInfoBuilder().title("mybatis培训接口文档")
				.description("mybatis培训人员查阅")
				.contact(new Contact("lcl", null, null))
				.version("版本号:1.0")
				.build();

		Docket docket = new Docket(DocumentationType.SWAGGER_2).groupName("mybatis培训API");
		docket.apiInfo(api).select()
				.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
				.paths(PathSelectors.any())
				.build();
		return docket;
	}

}
