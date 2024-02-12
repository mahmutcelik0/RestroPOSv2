package com.restropos.systemcore.entity;

import com.restropos.systemshop.entity.user.BasicUser;
import com.restropos.systemshop.entity.user.SystemUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.security.Timestamp;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;

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
    @JoinColumn(name = "BASIC_USER_ID")
    private BasicUser basicUser;

    @ManyToOne
    @JoinColumn(name = "SYSTEM_USER_ID")
    private SystemUser systemUser;

    public SecureToken(String token, LocalDateTime creationTimeStamp, LocalDateTime expireAt, BasicUser basicUser) {
        this.token = token;
        this.creationTimeStamp = creationTimeStamp;
        this.expireAt = expireAt;
        this.basicUser = basicUser;
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
