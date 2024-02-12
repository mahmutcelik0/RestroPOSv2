package com.restropos.systemshop.entity.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class EmailSecuredUser extends GenericUser{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    @Email
    @NotEmpty(message = "EMAIL IS REQUIRED FIELD")
    private String email;

//    @Pattern(regexp = "^(?=.*[a-zA-ZçÇğĞıİöÖşŞüÜ])(?=.*[A-ZÇĞİÖŞÜçğıöşü])(?=.*[0-9])(?=.*[._])[a-zA-Z0-9çÇğĞıİöÖşŞüÜ._]{8,}$")
    @NotEmpty(message = "PASSWORD IS REQUIRED FIELD")
    private String password;
}
