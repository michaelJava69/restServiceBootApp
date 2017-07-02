package org.rbsg.java.config;

 



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.context.ApplicationContext;
 

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import static com.google.common.collect.Lists.*;
import static springfox.documentation.schema.AlternateTypeRules.*;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
 



@ComponentScan({"org.rbsg.java.controller"})
@EnableAutoConfiguration
@EnableCaching
@EnableSwagger2     //1
@SpringBootApplication
public class Application {



    public static void main(String[] args) {

    	  SpringApplication.run(Application.class, args);
        
    }

/***
 1  Enables Springfox swagger 2

 2  Instructs spring where to scan for API controllers

 3  Docket, Springfox’s, primary api configuration mechanism is initialized for swagger specification 2.0

 4  select() returns an instance of ApiSelectorBuilder to give fine grained control over the endpoints exposed via swagger.

 5  apis() allows selection of RequestHandler's using a predicate. The example here uses an any predicate (default). 
    Out of the box predicates provided are any, none, withClassAnnotation, withMethodAnnotation and basePackage.

 6  paths() allows selection of Path's using a predicate. The example here uses an any predicate (default)

 7  The selector requires to be built after configuring the api and path selectors. Out of the box we provide predicates for 
    regex, ant, any, none

 8  Adds a servlet path mapping, when the servlet has a path mapping. this prefixes paths with the provided path mapping

 9  Convenience rule builder substitutes LocalDate with String when rendering model properties

 10 Convenience rule builder that substitutes a generic type with one type parameter with the type parameter. In this example
    ResponseEntity<T> with T. alternateTypeRules allows custom rules that are a bit more involved. The example substitutes 
    DeferredResult<ResponseEntity<T>> with T generically

 11  Flag to indicate if default http response codes need to be used or not

 12  Allows globally overriding response messages for different http methods. In this example we override the 500 error code 
     for all `GET`s …​

 13  … ​and indicate that it will use the response model Error (which will be defined elsewhere)

 14 Sets up the security schemes used to protect the apis. Supported schemes are ApiKey, BasicAuth and OAuth

 15 Provides a way to globally set up security contexts for operation. The idea here is that we provide a way to select operations 
    to be protected by one of the specified security schemes.

 16 Here we use ApiKey as the security schema that is identified by the name mykey

 17 Selector for the paths this security context applies to.

 18 Here we same key defined in the security scheme mykey

 19 Optional swagger-ui security configuration for oauth and apiKey settings

 20 Optional swagger-ui ui configuration currently only supports the validation url

    * Incubating * setting this flag signals to the processor that the paths generated should try and use form style 
    
 21 query expansion. As a result we could distinguish paths that have the same path stem but different query string 
    combinations. An example of this would be two apis:
    Allows globally configuration of default path-/request-/headerparameters which are common for every rest 
    
 22 operation of the api, but aren`t needed in spring controller method signature (for example authenticaton information).
    Parameters added here will be part of every API Operation in the generated swagger specification.

 23 How do you want to transport the api key via a HEADER (header) or QUERY_PARAM (query)?

 24 What header key needs to be used to send the api key. By default this value is set to api_key. Depending on how the 
    security is setup the name of the header used may need to be different. Overriding this value is a way to override the
    default behavior.

 25 Adding tags is a way to define all the available tags that services/operations can opt into. Currently this only has name
    and description.
    Are there models in the application that are not "reachable"? Not reachable is when we have models that we would like to be
    described but aren’t explicitly used in any operation. An example of this is an operation that 
    
 26 returns a model serialized as a string. We do want to communicate the expectation of the schema for the string. This is a way
    to do exactly that. There are plenty of more options to configure the Docket. This should provide a good start.
    
 ***/   
    
  /** 
    
    @Bean
    public Docket primesApi() {
      return new Docket(DocumentationType.SWAGGER_2) //3
          .select()  //4
            .apis(RequestHandlerSelectors.any()) //5
            .paths(PathSelectors.any())  //6
            .build()  //7
          .pathMapping("/")  //8
          .directModelSubstitute(LocalDate.class,
              String.class)  //9
          .genericModelSubstitutes(ResponseEntity.class)
          //.alternateTypeRules(
          //    newRule(typeResolver.resolve(DeferredResult.class,
          //            typeResolver.resolve(ResponseEntity.class, WildcardType.class)),
          //        typeResolver.resolve(WildcardType.class)))   //10
          .useDefaultResponseMessages(false)
          .globalResponseMessage(RequestMethod.GET,
              newArrayList(new ResponseMessageBuilder()
                  .code(500)
                  .message("500 message")
                  .responseModel(new ModelRef("Error"))
                  .build()))
          //.securitySchemes(newArrayList(apiKey()))
          //.securityContexts(newArrayList(securityContext()))
          .enableUrlTemplating(true)
          .globalOperationParameters(
              newArrayList(new ParameterBuilder()
                  .name("someGlobalParameter")
                  .description("Description of someGlobalParameter")
                  .modelRef(new ModelRef("string"))
                  .parameterType("query")
                  .required(true)
                  .build()))
          .tags(new Tag("Primes numbers Service", "All apis relating to primes numbers")) 
          //.additionalModels(typeResolver.resolve(AdditionalModel.class)) 
          ;
    }

   
**/
	 
    @Bean
    public Docket documentation() {
        return new Docket(DocumentationType.SWAGGER_2)  //3
          .select()   //4
            .apis(RequestHandlerSelectors.any())  //5
            .paths(PathSelectors.any())  //6
            .build()  //7
          .pathMapping("/")  //8
          .apiInfo(metadata())         
          .directModelSubstitute(LocalDate.class,
              String.class)  //9
          .genericModelSubstitutes(ResponseEntity.class)
          .useDefaultResponseMessages(false)
          //.globalResponseMessage(RequestMethod.GET,
          //        newArrayList(new ResponseMessageBuilder()
          //            .code(500)
          //            .message("500 message")
          //            .responseModel(new ModelRef("Error"))
          //            .build()))
          .enableUrlTemplating(true)  // 21
          .tags(new Tag("Primes numbers Service", "All apis relating to primes numbers")) 
          ;
    }
    
    private ApiInfo metadata() {
        return new ApiInfoBuilder()
          .title("My awesome API Prime Numbers API")
          .description("This is a Prime Number Service")
          .version("1.0")   
          .build()
          
          
          ;
      }
	
	  
 

	//@Autowired
	 //private TypeResolver typeResolver ;
	    

    private ApiKey apiKey() {
      return new ApiKey("mykey", "api_key", "header");
    }

    private SecurityContext securityContext() {
      return SecurityContext.builder()
          .securityReferences(defaultAuth())
          .forPaths(PathSelectors.regex("/anyPath.*"))
          .build();
    }

    List<SecurityReference> defaultAuth() {
      AuthorizationScope authorizationScope
          = new AuthorizationScope("global", "accessEverything");
      AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
      authorizationScopes[0] = authorizationScope;
      return newArrayList(
          new SecurityReference("mykey", authorizationScopes));
    }

   

    
    private List<SecurityReference> newArrayList(SecurityReference securityReference) {
		// TODO Auto-generated method stub
		return (List<SecurityReference>) securityReference;
	}

//	@Bean
//    SecurityConfiguration security() {
//      return new SecurityConfiguration(
//          "test-app-client-id",
//          "test-app-client-secret",
//          "test-app-realm",
//          "test-app",
//          "apiKey",
//          ApiKeyVehicle.HEADER, 
//          "api_key", 
//          "," /*scope separator*/);
//    }
 
      
    /**
     * SWAGGER
     * Changed from 2.4.0  (now using 2.7.0)  Constructor now has a8 parameters
     * @return
     */
    /*
    @Bean
    UiConfiguration uiConfig() {
      return new UiConfiguration(
          "validatorUrl",// url
          "none",       // docExpansion          => none | list
          "alpha",      // apiSorter             => alpha
          "schema",     // defaultModelRendering => schema
          UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS,
          false,        // enableJsonEditor      => true | false
          true,         // showRequestHeaders    => true | false
          60000L );      // requestTimeout => in milliseconds, defaults to null (uses jquery xh timeout)
   }
    
    */
    @Bean
    UiConfiguration uiConfig() {
    	return new UiConfiguration(null);
    }
    
    /**
     * Caching setup for annotations to work
     * 
     * @return
     * @throws IOException
     */

    @Bean

    public CacheManager cacheManager() throws IOException {

        return new EhCacheCacheManager(ehCacheCacheManager().getObject());

    }



    @Bean

    public EhCacheManagerFactoryBean ehCacheCacheManager() throws IOException {

        EhCacheManagerFactoryBean cmfb = new EhCacheManagerFactoryBean();

        cmfb.setConfigLocation(new ClassPathResource("ehcache.xml"));

        cmfb.setCacheManagerName("primes");

        cmfb.setShared(true);

        cmfb.afterPropertiesSet();

        return cmfb;

    }



}
