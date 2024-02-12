package com.restropos.systemcore.security;

import com.restropos.systemshop.constants.UserTypes;
import com.restropos.systemshop.entity.Role;
import com.restropos.systemshop.entity.user.BasicUser;
import com.restropos.systemshop.entity.user.EmailSecuredUser;
import com.restropos.systemshop.entity.user.SystemUser;
import com.restropos.systemshop.service.BasicUserService;
import com.restropos.systemshop.service.CustomerService;
import com.restropos.systemshop.service.SystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Optional<SystemUser> systemUser = systemUserService.findOptionalSystemUserByEmail(authentication.getPrincipal().toString());
        Optional<BasicUser> basicUser = basicUserService.findOptionalBasicUserByEmail(authentication.getPrincipal().toString());

        if(systemUser.isPresent()){
            return getUsernamePasswordAuthenticationTokenWithEmail(authentication, systemUser.get());
        }else if (basicUser.isPresent()){
            return getUsernamePasswordAuthenticationTokenWithEmail(authentication, basicUser.get());
        }else {
            return null;
        }
        //customer için de eklee
    }

    private UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationTokenWithEmail(Authentication authentication, EmailSecuredUser emailSecuredUser) {
        boolean matches = passwordEncoder.matches(authentication.getCredentials().toString(), emailSecuredUser.getPassword()); //Credentials == password

        if (!matches) throw new BadCredentialsException("WRONG LOGIN INFORMATION PROVIDED");
        else if(emailSecuredUser.isLoginDisabled()) throw new RuntimeException("YOU HAVE TO VERIFY YOUR ACCOUNT");

        return new UsernamePasswordAuthenticationToken(emailSecuredUser.getEmail(), emailSecuredUser.getPassword(), List.of(new SimpleGrantedAuthority(emailSecuredUser.getRole().getRoleName())));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication)); //copy from AbstractUserDetailsAuthProvider
    }
}

/**
 *
 * if (userType.equals(UserTypes.ADMIN) || userType.equals(UserTypes.WAITER)) {
 *             SystemUser systemUser = systemUserService.findSystemUserByEmail(username);
 *
 *             return new org.springframework.security.core.userdetails.User(systemUser.getEmail(), systemUser.getPassword(), List.of(new SimpleGrantedAuthority(systemUser.getRole().getRoleName())));
 *         } else if (userType.equals(UserTypes.KITCHEN) || userType.equals(UserTypes.CASH_DESK)) {
 *             BasicUser basicUser = basicUserService.findBasicUserByEmail(username);
 *
 *             return new org.springframework.security.core.userdetails.User(basicUser.getEmail(), basicUser.getPassword(), List.of(new SimpleGrantedAuthority(basicUser.getRole().getRoleName())));
 *         } else if (userType.equals(UserTypes.CUSTOMER)) {
 *             Customer customer = customerService.findCustomerByPhoneNUmber(username);
 *
 *             return new org.springframework.security.core.userdetails.User(customer.getPhoneNumber(),customer.getPhoneNumber(), List.of(new SimpleGrantedAuthority(customer.getRole().getRoleName())));
 *         }
 *
 * */
