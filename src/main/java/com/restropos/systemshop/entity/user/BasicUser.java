package com.restropos.systemshop.entity.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "BASIC_USERS")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "USER_TYPE",discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BasicUser extends EmailSecuredUser{
    @Column(name = "DEVICE_NAME",nullable = false)
    @NotEmpty
    private String deviceName;

    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH},fetch = FetchType.EAGER)
    @JoinColumn(name = "WORKSPACE_NAME",referencedColumnName = "BUSINESS_NAME")
    private Workspace workspace;

    @Override
    boolean isSecured() {
        return false;
    }
}
