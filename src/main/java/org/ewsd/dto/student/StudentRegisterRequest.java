package org.ewsd.dto.student;

import lombok.Data;

@Data
public class StudentRegisterRequest {

    private String email; //where email will be sent
    private String eduEmail; // student login email
//    private String password;
    private String password;
    private String firstName;
    private String lastName;

    private Integer age;
    private String grade;
}