package com.restropos.systemcore.security;

import com.restropos.systemcore.constants.CustomResponseMessage;
import com.restropos.systemcore.exception.VerificationRequiredException;
import com.restropos.systemshop.constants.UserTypes;
import com.restropos.systemshop.entity.user.BasicUser;
import com.restropos.systemshop.entity.user.EmailSecuredUser;
import com.restropos.systemshop.entity.user.SystemUser;
import com.restropos.systemshop.service.BasicUserService;
import com.restropos.systemshop.service.CustomerService;
import com.restropos.systemshop.service.SystemUserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private BasicUserService basicUserService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SystemUserService systemUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @SneakyThrows
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Optional<SystemUser> systemUser = systemUserService.findOptionalSystemUserByEmail(authentication.getPrincipal().toString());
        Optional<BasicUser> basicUser = basicUserService.findOptionalBasicUserByEmail(authentication.getPrincipal().toString());

        if(systemUser.isPresent()){
            return getUsernamePasswordAuthenticationTokenForSystemUser(authentication, systemUser.get());
        }else if(basicUser.isPresent()){
            return getUsernamePasswordAuthenticationToken(authentication,basicUser.get());
        }else if(customerService.checkCustomerExists(authentication.getPrincipal().toString())){
            return new UsernamePasswordAuthenticationToken(authentication.getPrincipal(),"", List.of(new SimpleGrantedAuthority(UserTypes.CUSTOMER.getName())));
        }else {
            throw new RuntimeException(CustomResponseMessage.USER_NOT_FOUND);
        }
        //customer için de eklee
    }

    private UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationTokenForSystemUser(Authentication authentication, SystemUser systemUser) throws VerificationRequiredException {
        if(systemUser.isLoginDisabled()) throw new VerificationRequiredException(CustomResponseMessage.VERIFICATION_REQUIRED);
        else {
            return getUsernamePasswordAuthenticationToken(authentication,systemUser);
        }
    }

    private UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(Authentication authentication,EmailSecuredUser emailSecuredUser) {
        boolean matches = passwordEncoder.matches(authentication.getCredentials().toString(), emailSecuredUser.getPassword()); //Credentials == password

        if (!matches) throw new BadCredentialsException(CustomResponseMessage.WRONG_CREDENTIAL);

        return new UsernamePasswordAuthenticationToken(emailSecuredUser.getEmail(), emailSecuredUser.getPassword(), List.of(new SimpleGrantedAuthority(emailSecuredUser.getRole().getRoleName())));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication)); //copy from AbstractUserDetailsAuthProvider
    }
}

