package com.restropos.systemmenu.entity;

import com.restropos.systemshop.entity.Image;
import com.restropos.systemshop.entity.Workspace;
import jakarta.persistence.*;
import lombok.*;

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
}
