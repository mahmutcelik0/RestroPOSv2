package com.restropos.systemcore.service;

import com.restropos.systemcore.dto.EnableToken;
import com.restropos.systemcore.entity.SecureToken;
import com.restropos.systemcore.repository.SecureTokenRepository;
import com.restropos.systemshop.constants.UserTypes;
import com.restropos.systemshop.entity.user.BasicUser;
import com.restropos.systemshop.entity.user.SystemUser;
import com.restropos.systemshop.service.BasicUserService;
import com.restropos.systemshop.service.SystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SecureTokenService {
    @Autowired
    private SecureTokenRepository secureTokenRepository;

    @Autowired
    private BasicUserService basicUserService;

    @Autowired
    private SystemUserService systemUserService;

    public SecureToken generateTokenForSystemUser(String adminEmail) {
        SystemUser systemUser = systemUserService.findSystemUserByEmail(adminEmail);

        SecureToken secureToken = new SecureToken(generateRandomCode(), LocalDateTime.now(), LocalDateTime.now().plusMinutes(5), systemUser);
        return generateToken(secureToken);
    }

    public SecureToken generateTokenForBasicUser(BasicUser basicUser) {
        SecureToken secureToken = new SecureToken(generateRandomCode(), LocalDateTime.now(), LocalDateTime.now().plusMinutes(5), basicUser);
        return generateToken(secureToken);
    }

    private SecureToken generateToken(SecureToken secureToken) {
        secureTokenRepository.existsSecureTokenByToken(secureToken.getToken());

        while (secureTokenRepository.existsSecureTokenByToken(secureToken.getToken())){
            secureToken.setToken(generateRandomCode());
        }

        return secureTokenRepository.save(secureToken);
    }


    public String  generateRandomCode() {
        SecureRandom random = new SecureRandom();
        return String.valueOf(random.nextInt(100000, 999999));
    }

    public ResponseEntity<?> enableAccountWithToken(EnableToken enableToken) {
        List<SecureToken> secureTokens = secureTokenRepository.findSecureTokenByUserEmail(enableToken.getAccountInformation());

        if (CollectionUtils.isEmpty(secureTokens)) return new ResponseEntity<>("q",HttpStatus.NOT_ACCEPTABLE);
        var token = secureTokens.get(0);

        if(token.isExpired()) return new ResponseEntity<>("Time is up",HttpStatus.NOT_ACCEPTABLE);
        else if(!token.getToken().equals(enableToken.getTokenCode())) {
            //deneme hakkından azaltılacak 0 olunca token i sil
            return new ResponseEntity<>("Wrong code",HttpStatus.NOT_ACCEPTABLE);
        }else {
            var userRoleName = ObjectUtils.isEmpty(token.getBasicUser())?token.getSystemUser().getRole().getRoleName():token.getBasicUser().getRole().getRoleName();
            if(userRoleName.equals(UserTypes.ADMIN.getName()) || userRoleName.equals(UserTypes.WAITER.getName())){
                var user  = systemUserService.findSystemUserByEmail(token.getSystemUser().getEmail());
                user.setLoginDisabled(false);
                systemUserService.save(user);
            } else if (userRoleName.equals(UserTypes.CASH_DESK.getName()) || userRoleName.equals(UserTypes.KITCHEN.getName())) {
                var user  = basicUserService.findBasicUserByEmail(token.getBasicUser().getEmail());
                user.setLoginDisabled(false);
                basicUserService.save(user);
            }
        }
        return new ResponseEntity<>("Account enabled",HttpStatus.ACCEPTED);

    }
}
