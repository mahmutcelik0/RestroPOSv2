package com.restropos.systemshop.api;

import com.restropos.systemcore.dto.BearerToken;
import com.restropos.systemcore.dto.LoginDto;
import com.restropos.systemcore.security.UsernamePasswordAuthenticationProvider;
import com.restropos.systemcore.utils.JwtTokenUtil;
import com.restropos.systemshop.dto.EmailSecuredUserDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthApi {
    @Autowired
    private UsernamePasswordAuthenticationProvider providerManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginDto loginDto) {
        Authentication authentication = providerManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtTokenUtil.generateTokenForEmail(new EmailSecuredUserDto(authentication.getPrincipal().toString()), authentication.getAuthorities().stream().findFirst().get().toString());

        BearerToken bearerToken = new BearerToken(accessToken, "Bearer ");

        return ResponseEntity.ok(bearerToken);
    }
}
