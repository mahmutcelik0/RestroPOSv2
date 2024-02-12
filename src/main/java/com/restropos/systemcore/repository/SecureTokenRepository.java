package com.restropos.systemcore.repository;

import com.restropos.systemcore.entity.SecureToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SecureTokenRepository extends JpaRepository<SecureToken,Long> {

    boolean existsSecureTokenByToken(String token);

    @Query("select st from SecureToken as st left join BasicUser b on(st.basicUser = b) left join SystemUser as s on(st.systemUser = s) where b.email = ?1 or s.email = ?1")
    List<SecureToken> findSecureTokenByUserEmail(String email);
}
