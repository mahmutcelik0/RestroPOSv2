package com.restropos.systemcore.entity;

import com.restropos.systemshop.entity.user.Customer;
import com.restropos.systemshop.entity.user.SystemUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "SECURE_TOKENS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecureToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String token;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime creationTimeStamp;

    @Column(updatable = false)
    @Basic(optional = false)
    private LocalDateTime expireAt;

    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "SYSTEM_USER_ID")
    private SystemUser systemUser;

    public SecureToken(String token, LocalDateTime creationTimeStamp, LocalDateTime expireAt, Customer customer) {
        this.token = token;
        this.creationTimeStamp = creationTimeStamp;
        this.expireAt = expireAt;
        this.customer = customer;
    }

    public SecureToken(String token, LocalDateTime creationTimeStamp, LocalDateTime expireAt, SystemUser systemUser) {
        this.token = token;
        this.creationTimeStamp = creationTimeStamp;
        this.expireAt = expireAt;
        this.systemUser = systemUser;
    }

    public boolean isExpired(){
        return this.expireAt.isBefore(LocalDateTime.now());
    }
}
