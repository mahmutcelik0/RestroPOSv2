package com.restropos.systemmenu.entity;

import com.restropos.systemorder.entity.Order;
import com.restropos.systemshop.entity.Image;
import com.restropos.systemshop.entity.Workspace;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "WORKSPACE_TABLES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkspaceTable {
    @Id
    private String tableId;

    private String tableName;

    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name = "BUSINESS_DOMAIN",referencedColumnName = "BUSINESS_DOMAIN")
    private Workspace workspace;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "WORKSPACE_TABLE_IMAGE")
    private Image image;

    @OneToMany(cascade = {CascadeType.ALL},mappedBy = "workspaceTable")
    private List<Order> orders = new ArrayList<>();
}
