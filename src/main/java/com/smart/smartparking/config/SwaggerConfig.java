package com.smart.smartparking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 常用注解：
 * - @Api()用于类；
 * 表示标识这个类是swagger的资源
 * - @ApiOperation()用于方法；
 * 表示一个http请求的操作
 * - @ApiParam()用于方法，参数，字段说明；
 * 表示对参数的添加元数据（说明或是否必填等）
 * - @ApiModel()用于类
 * 表示对类进行说明，用于参数用实体类接收
 * - @ApiModelProperty()用于方法，字段
 * 表示对model属性的说明或者数据操作更改
 * - @ApiIgnore()用于类，方法，方法参数
 * 表示这个方法或者类被忽略
 * - @ApiImplicitParam() 用于方法
 * 表示单独的请求参数
 * - @ApiImplicitParams() 用于方法，包含多个 @ApiImplicitParam
 */

@Configuration
@EnableSwagger2
public class SwaggerConfig {
  //  @Value("${swagger.basePackage:''}")
    private String basePackage="com.smart.smartparking.";
 //   @Value("${swagger.appName:''}")
    private String appName="smart-parking";
    @Bean
    public Docket createRestApi() {// 创建API基本信息
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackage))// 扫描该包下的所有需要在Swagger中展示的API，@ApiIgnore注解标注的除外
                .paths(PathSelectors.any())
                .build();
    }
    private ApiInfo apiInfo() {// 创建API的基本信息，这些信息会在Swagger UI中进行显示
        return new ApiInfoBuilder()
                .title("Swagger2构建RESTful APIs")// API 标题
                .description(appName + "：提供的RESTful APIs")// API描述
                .version("1.0")// 版本号
                .build();
    }
}
