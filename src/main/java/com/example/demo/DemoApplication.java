package com.example.demo;


import org.lamb.framework.common.util.sample.JsonUtil;
import org.lamb.framework.web.core.handler.LambResponseHandler;
import org.lamb.framework.web.security.LambPrincipalUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;


@SpringBootApplication(scanBasePackages = {"com.example","org.lamb.framework"} )
@EnableWebFlux
@RestController
@RequestMapping("/test")
public class DemoApplication extends LambResponseHandler  {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Resource
    private LambPrincipalUtil lambPrincipalUtil;




    @GetMapping("/login")
    public Mono login(){  //1
        User user =  User.builder().age(30).name("王刚").school("兰州理工大学").build();
        String userJson = JsonUtil.objToString(user);
        String authToken = lambPrincipalUtil.setPrincipalToToken(userJson);
        return returning(authToken);
    }

    @GetMapping("/testSecurity1")
    public Mono testSecurity1(){  //1

        return returning();
    }




    @Bean
    RouterFunction<ServerResponse> userRouterFunction(UserHandler userHandler) {
        return RouterFunctions.nest(RequestPredicates.path("/test")
                ,RouterFunctions.route(RequestPredicates.GET("/testSecurity2"), userHandler::testSecurity2)
        );
    }

    //路由写法
    @Component
    class UserHandler{
        public Mono<ServerResponse> testSecurity2(ServerRequest serverRequest) {
            return routing();
        }
    }

    public Flux test(){

        Flux<Integer> flux1 = Flux.just(1,2,0);
        Flux<String> flux2 = Flux.just("1","2","3");

        return Flux.zip(flux1,flux2)
        .flatMap(data->{
                return Mono.just(data.getT1());
        });
    }
}
