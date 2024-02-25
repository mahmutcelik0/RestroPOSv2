package com.restropos.systemshop.entity.user;

import com.restropos.systemcore.constants.CustomResponseMessage;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "[A-Za-z0-9](?:[A-Za-z0-9\\-]{0,61}[A-Za-z0-9])?",message = CustomResponseMessage.BUSINESS_DOMAIN_PATTERN)
    private String businessDomain;

    private String businessName;

    private byte[] businessLogo;

    @OneToMany(cascade = CascadeType.ALL , fetch = FetchType.LAZY,mappedBy = "workspace")
    private List<BasicUser> basicUsers;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "workspace")
    private List<SystemUser> systemUsers;
}
