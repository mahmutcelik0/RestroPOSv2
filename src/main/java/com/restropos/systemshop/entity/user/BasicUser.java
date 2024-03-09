package com.restropos.systemshop.entity.user;

import com.restropos.systemshop.entity.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "BASIC_USERS")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "USER_TYPE", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class BasicUser extends EmailSecuredUser {
    @Column(name = "DEVICE_NAME", nullable = false)
    @NotEmpty
    private String deviceName;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "WORKSPACE_NAME", referencedColumnName = "BUSINESS_DOMAIN")
    private Workspace workspace;

    public BasicUser(String email, String password, Role role, String deviceName, Workspace workspace) {
        super(email, password, role);
        this.deviceName = deviceName;
        this.workspace = workspace;
    }
}
