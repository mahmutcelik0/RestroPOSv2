package com.restropos.systemshop.entity.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "CUSTOMERS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends GenericUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    @Column(name = "FIRST_NAME",nullable = false,columnDefinition = "nvarchar(50)")
    @NotBlank
    @Size(min = 3,max = 50)
    private String firstName;

    @Column(name = "LAST_NAME",nullable = false,columnDefinition = "nvarchar(50)")
    @NotBlank
    @Size(min = 3,max = 50)
    private String lastName;

    @Column(name = "PROFILE_PHOTO")
    private byte[] profilePhoto;

    //@Pattern(regexp = "+") //todo TEL NO PATTERN I AYARLANACAK
    @Column(name = "PHONE_NUMBER", nullable = false, columnDefinition = "nvarchar(13)")
    @Size(min = 13, max = 13)
    private String phoneNumber;


    @Override
    boolean isSecured() {
        return false;
    }
}
