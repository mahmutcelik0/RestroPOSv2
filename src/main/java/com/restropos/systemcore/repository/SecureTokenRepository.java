package com.restropos.systemcore.repository;

import com.restropos.systemcore.entity.SecureToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SecureTokenRepository extends JpaRepository<SecureToken,Long> {

    boolean existsSecureTokenByToken(String token);

    @Query("select st from SecureToken as st left join SystemUser as s on(st.systemUser = s) left join Customer as c on (st.customer = c) where (s.email = ?1 or c.phoneNumber = ?1) and st.token = ?2")
    Optional<SecureToken> findSecureTokenByAccountInformation(String accountInformation,String tokenCode);
}
