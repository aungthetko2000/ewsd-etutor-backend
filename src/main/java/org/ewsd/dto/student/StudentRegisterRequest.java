package org.ewsd.dto.student;

import lombok.Data;

@Data
public class StudentRegisterRequest {

    private String email;
    private String password;
    private String firstName;
    private String lastName;

    private Integer age;
    private String grade;
}