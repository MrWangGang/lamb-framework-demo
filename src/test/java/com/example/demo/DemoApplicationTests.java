package com.example.demo;

import org.lamb.framework.common.templete.LambResponseTemplete;
import org.lamb.framework.common.util.sample.JsonUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@SpringBootTest
class DemoApplicationTests {


    public static void main(String[] args) throws InterruptedException {
    test1();
    }
    //test1
    public static void test1() {
        Mono<String> loginRsp = WebClient.create()
                .method(HttpMethod.GET)
                .uri("http://localhost:8080/test/login")
                .retrieve().bodyToMono(String.class);

        LambResponseTemplete lambResponseTemplete = JsonUtil.stringToObj(loginRsp.block(), LambResponseTemplete.class).get();
        String token = lambResponseTemplete.getData().toString();

        Mono<String> AuthRsp = WebClient.create()
                .method(HttpMethod.GET)
                .uri("http://localhost:8080/test/testSecurity2")
                .header("Auth-Token",token)
                .retrieve().bodyToMono(String.class);
        System.out.println(JsonUtil.stringToObj(AuthRsp.block(), LambResponseTemplete.class).get());

    }

}
