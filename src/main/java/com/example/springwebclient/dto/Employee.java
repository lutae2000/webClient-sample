package com.example.springwebclient.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    private Integer age;
    private String firstName;
    private String lastName;
    private String gender;
    private Integer id;
    private String role;
}
