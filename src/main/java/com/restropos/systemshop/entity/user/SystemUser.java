package com.restropos.systemshop.entity.user;


import com.restropos.systemshop.entity.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "SYSTEM_USERS")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "USER_TYPE",discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SystemUser extends EmailSecuredUser{
    @Column(name = "FIRST_NAME",nullable = false,columnDefinition = "nvarchar(50)")
    @NotBlank
    @Size(min = 2,max = 50)
    private String firstName;

    @Column(name = "LAST_NAME",nullable = false,columnDefinition = "nvarchar(50)")
    private String lastName;

    @Column(name = "LOGIN_DISABLED",nullable = false)
    private boolean loginDisabled = true;

    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH},fetch = FetchType.EAGER)
    @JoinColumn(name = "WORKSPACE_NAME",referencedColumnName = "BUSINESS_DOMAIN")
    private Workspace workspace;

    public SystemUser(Long id,@Email @NotEmpty(message = "EMAIL IS REQUIRED FIELD") String email, @NotEmpty(message = "PASSWORD IS REQUIRED FIELD") String password, String firstName, String lastName, boolean loginDisabled, Workspace workspace, Role role) {
        super(id,email, password ,role);
        this.firstName = firstName;
        this.lastName = lastName;
        this.loginDisabled = loginDisabled;
        this.workspace = workspace;
    }

    public SystemUser(@Email @NotEmpty(message = "EMAIL IS REQUIRED FIELD") String email, @NotEmpty(message = "PASSWORD IS REQUIRED FIELD") String password, String firstName, String lastName, boolean loginDisabled, Workspace workspace, Role role) {
        super(email, password ,role);
        this.firstName = firstName;
        this.lastName = lastName;
        this.loginDisabled = loginDisabled;
        this.workspace = workspace;
    }

}
