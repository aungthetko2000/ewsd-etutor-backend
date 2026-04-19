package org.ewsd.dto.student;

import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentRegisterRequest {

    private String firstName;
    private String lastName;
    private String fatherName;
    private LocalDate dob;
    private String gender;
    private String email;
    private String phone;
    private String emergencyContact;
    private String session;
    private String address;
    private String course;
    private LocalDate registrationDate;

}