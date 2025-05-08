package com.rdcl.Usersapi.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "persona")
@Inheritance(strategy = InheritanceType.JOINED)

public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer perId;
    @Column(nullable = false)
    private String perName;
    @Column(nullable = false, length = 1)
    private String perGender;
    @Column(nullable = false)
    private Integer perAge;
    @Column(nullable = false, unique = true, length = 13)
    private String perIdentification;
    @Column(nullable = false)
    private String perAddress;
    @Column(nullable = false,length = 10)
    private String perNumberPhone;
}