package com.restropos.systemshop.entity.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "CUSTOMERS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    @Column(name = "FIRST_NAME",nullable = false,columnDefinition = "nvarchar(50)")
    @NotBlank
    @Size(min = 2,max = 50)
    private String firstName;

    @Column(name = "LAST_NAME",nullable = false,columnDefinition = "nvarchar(50)")
    @NotBlank
    @Size(min = 2,max = 50)
    private String lastName;

    @Column(name = "PROFILE_PHOTO")
    private byte[] profilePhoto;

    //@Pattern(regexp = "+") //todo TEL NO PATTERN I AYARLANACAK
    @Column(name = "PHONE_NUMBER", nullable = false, columnDefinition = "nvarchar(12)")
    @Size(min = 12, max = 12)
    private String phoneNumber;

    @Column(name = "LOGIN_DISABLED",nullable = false)
    @Builder.Default
    private boolean loginDisabled = true;

}
