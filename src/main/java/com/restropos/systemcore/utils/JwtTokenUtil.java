package com.restropos.systemcore.utils;


import com.restropos.systemshop.dto.EmailSecuredUserDto;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtTokenUtil {
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role")).toString();
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    //todo check this code snippet
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String subject = extractSubject(token);
        return (subject.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private String generateToken(String subject, String role) {

        return Jwts.builder()
                .setSubject(subject)
                .claim("role", role)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(Date.from(Instant.now().plus(expiration, ChronoUnit.MILLIS)))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String generateTokenForEmail(EmailSecuredUserDto emailSecuredUserDto,String role){
        return generateToken(emailSecuredUserDto.getEmail(),role);
    }

    public String generateTokenForPhoneNumber(String phoneNumber, String role){
        return generateToken(phoneNumber,role);
    }


    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            LogUtil.printLog("Invalid JWT signature.", JwtTokenUtil.class);
        } catch (MalformedJwtException e) {
            LogUtil.printLog("Invalid JWT token.", JwtTokenUtil.class);
        } catch (ExpiredJwtException e) {
            LogUtil.printLog("Expired JWT token.", JwtTokenUtil.class);
        } catch (UnsupportedJwtException e) {
            LogUtil.printLog("Unsupported JWT token.", JwtTokenUtil.class);
        } catch (IllegalArgumentException e) {
            LogUtil.printLog("JWT token compact of handler are invalid.", JwtTokenUtil.class);
        }
        return false;
    }

    public String getToken(HttpServletRequest httpServletRequest) {
        final String bearerToken = httpServletRequest.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // The part after "Bearer "
        }
        return null;
    }
}