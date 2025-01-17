package com.restropos.systemshop.entity;

import com.restropos.systemcore.constants.CustomResponseMessage;
import com.restropos.systemmenu.entity.Category;
import com.restropos.systemmenu.entity.Product;
import com.restropos.systemshop.entity.user.SystemUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "WORKSPACES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Workspace {
    @Id
    @Column(name = "BUSINESS_DOMAIN",length = 50)
    @Pattern(regexp = "[A-Za-z0-9](?:[A-Za-z0-9\\-]{0,61}[A-Za-z0-9])?",message = CustomResponseMessage.BUSINESS_DOMAIN_PATTERN)
    private String businessDomain;

    private String businessName;

    @OneToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name = "IMAGE",referencedColumnName = "imageName")
    private Image image;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "workspace")
    private List<SystemUser> systemUsers;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "workspace")
    private List<Category> categories;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "workspace")
    private List<Product> products;

    private Integer totalReviewCount;

    private Double meanOfWorkspaceStar;
}
