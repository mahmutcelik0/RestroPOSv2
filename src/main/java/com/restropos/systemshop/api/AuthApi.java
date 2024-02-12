package com.restropos.systemshop.api;

import com.restropos.systemcore.dto.BearerToken;
import com.restropos.systemcore.dto.EnableToken;
import com.restropos.systemcore.dto.LoginDto;
import com.restropos.systemcore.security.UsernamePasswordAuthenticationProvider;
import com.restropos.systemcore.service.SecureTokenService;
import com.restropos.systemcore.utils.JwtTokenUtil;
import com.restropos.systemshop.dto.EmailSecuredUserDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthApi {
    @Autowired
    private UsernamePasswordAuthenticationProvider providerManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private SecureTokenService secureTokenService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDto loginDto) {
        Authentication authentication = providerManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtTokenUtil.generateTokenForEmail(new EmailSecuredUserDto(authentication.getPrincipal().toString()), authentication.getAuthorities().stream().findFirst().get().toString());

        BearerToken bearerToken = new BearerToken(accessToken, "Bearer ");

        return ResponseEntity.ok(bearerToken);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(){
        return null;
    }

    @PostMapping("/account/enable")
    public ResponseEntity<?> enableAccountWithToken(@RequestBody EnableToken enableToken){
        return secureTokenService.enableAccountWithToken(enableToken);
    }
}
