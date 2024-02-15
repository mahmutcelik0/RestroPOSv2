package com.restropos.systemcore.filter;

import com.restropos.systemcore.security.CustomUserDetailsService;
import com.restropos.systemcore.utils.JwtTokenUtil;
import com.restropos.systemcore.utils.LogUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtUtilities;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtUtilities.getToken(request);

        if (token != null && jwtUtilities.validateToken(token)) {
            String subject = jwtUtilities.extractSubject(token);
            String roleName = jwtUtilities.extractRole(token);

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(subject,roleName);
            if (userDetails != null) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
                LogUtil.printLog("authenticated user with subject :"+subject, JwtAuthenticationFilter.class);
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }
        }
        filterChain.doFilter(request, response);
    }

}