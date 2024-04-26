package com.restropos.systemshop.entity.user;

import com.restropos.systemorder.entity.Order;
import com.restropos.systemshop.entity.Image;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "CUSTOMERS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    @Column(name = "FIRST_NAME",nullable = false,columnDefinition = "nvarchar(50)")
    @NotBlank
    @Size(min = 2,max = 50)
    private String firstName;

    @Column(name = "LAST_NAME",nullable = false,columnDefinition = "nvarchar(50)")
    @NotBlank
    @Size(min = 2,max = 50)
    private String lastName;

    @OneToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name = "PROFILE_PHOTO",referencedColumnName = "imageName")
    private Image image;

    //@Pattern(regexp = "+") //todo TEL NO PATTERN I AYARLANACAK
    @Column(name = "PHONE_NUMBER", nullable = false, columnDefinition = "nvarchar(12)")
    @Size(min = 12, max = 12)
    private String phoneNumber;

    @Column(name = "LOGIN_DISABLED",nullable = false)
    @Builder.Default
    private boolean loginDisabled = true;

    @OneToMany(cascade = {CascadeType.ALL},mappedBy = "customer")
    private List<Order> orders;

}
