package com.restropos.systemshop.entity.user;

import com.restropos.systemshop.entity.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class EmailSecuredUser{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "USER_ID")
    private Long id;

    @Email
    @NotEmpty(message = "EMAIL IS REQUIRED FIELD")
    private String email;

//    @Pattern(regexp = "^(?=.*[a-zA-ZçÇğĞıİöÖşŞüÜ])(?=.*[A-ZÇĞİÖŞÜçğıöşü])(?=.*[0-9])(?=.*[._])[a-zA-Z0-9çÇğĞıİöÖşŞüÜ._]{8,}$")
    @NotEmpty(message = "PASSWORD IS REQUIRED FIELD")
    private String password;

    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH},fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ROLE",referencedColumnName = "ROLE_NAME")
    private Role role;

    public EmailSecuredUser(String email, String password,Role role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
