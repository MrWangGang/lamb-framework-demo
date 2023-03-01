package com.example.demo;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class User {
    private String name;

    private String school;

    private Integer age;
}
