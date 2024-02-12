package com.restropos.systemshop.entity.user;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "SYSTEM_USERS")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "USER_TYPE",discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SystemUser extends EmailSecuredUser{
    @Column(name = "FIRST_NAME",nullable = false,columnDefinition = "nvarchar(50)")
    @NotBlank
    @Size(min = 3,max = 50)
    private String firstName;

    @Column(name = "LAST_NAME",nullable = false,columnDefinition = "nvarchar(50)")
    @NotBlank
    @Size(min = 3,max = 50)
    private String lastName;

    @Override
    boolean isSecured() {
        return false;
    }
}
