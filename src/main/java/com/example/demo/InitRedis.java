package com.example.demo;

import org.lamb.framework.redis.operation.LambReactiveRedisOperation;
import org.lamb.framework.web.security.manger.LambAuthManager;
import org.lamb.framework.web.security.manger.LambAutzManager;
import org.lamb.framework.web.security.manger.support.LambPermission;
import org.lamb.framework.web.security.manger.support.LambRole;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class InitRedis implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        init();
    }

    @Bean
    public LambAutzManager lambDemoAutzManager(LambAuthManager lambDemoAuthManager){
        return new LambAutzManager(lambDemoAuthManager) {
            @Override
            public boolean verify(String currentPathAutzTree, String principal) {
                return true;
            }
        };
    }
    @Bean
    public LambAuthManager lambDemoAuthManager(){
        return new LambAuthManager() {
            @Override
            public boolean verify(String principal) {
                return true;
            }
        };
    }
    @Resource(name = "lambAutzRedisTemplate")
    private ReactiveRedisTemplate lambAutzRedisTemplate;

    private void init(){
        //init
        LambPermission permission1 = LambPermission.builder().id(0l).name("INSERT").build();
        LambPermission permission2 = LambPermission.builder().id(1l).name("VIEW").build();
        LambPermission permission3 = LambPermission.builder().id(2l).name("DELETE").build();
        LambPermission permission4 = LambPermission.builder().id(3l).name("UPDATE").build();

        List roleAdminPerslist = new ArrayList<>();
        roleAdminPerslist.add(permission1);
        roleAdminPerslist.add(permission2);
        roleAdminPerslist.add(permission3);
        roleAdminPerslist.add(permission4);

        List roleVipPerslist = new ArrayList<>();
        roleVipPerslist.add(permission2);

        LambRole admin = LambRole.builder().id(0L).name("admin").permissions(roleAdminPerslist).build();
        LambRole vip = LambRole.builder().id(1L).name("vip").permissions(roleVipPerslist).build();
        LambRole guest = LambRole.builder().build();


        //LambReactiveRedisOperation.build(lambAutzRedisTemplate).set("/test/testSecurity1",admin,600L);
        LambReactiveRedisOperation.build(lambAutzRedisTemplate).set("/test/testSecurity2",vip,600L);
        LambReactiveRedisOperation.build(lambAutzRedisTemplate).set("/test/testSecurity3",guest,600L);
    }
}
