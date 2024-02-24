package com.restropos.systemshop.entity.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "WORKSPACES")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Workspace {
    @Id
    @Column(name = "BUSINESS_DOMAIN")
    private String businessDomain;

    private String businessName;

    private byte[] businessLogo;

    @OneToMany(cascade = CascadeType.ALL , fetch = FetchType.LAZY,mappedBy = "workspace")
    private List<BasicUser> basicUsers;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "workspace")
    private List<SystemUser> systemUsers;
}
