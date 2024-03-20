package com.restropos.systemshop.entity;

import com.restropos.systemimage.constants.FolderEnum;
import com.restropos.systemshop.entity.user.Customer;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "IMAGES")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    @Id
    private String imageName;

    @Enumerated
    private FolderEnum folderName;

    @Column(name = "IMAGE_LINK")
    private String link;

    @OneToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH},mappedBy = "image")
    private Workspace workspace;

    @OneToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH},mappedBy = "image")
    private Customer customer;

    public Image(String imageName, FolderEnum folderName, String link) {
        this.imageName = imageName;
        this.folderName = folderName;
        this.link = link;
    }
}
