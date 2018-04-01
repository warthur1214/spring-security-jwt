package com.warthur.jwt.framework.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@Data
public class TokenAuthenticationService {

    private static final long EXPIRE_TIME = 2 * 3600 * 1000;
    private static final String SECRET = "qwerASDFzxcv";
    private static final String TOKEN_PREFIX = "Basic ";
    private static final String REQUEST_HEADER = "Authorization";

    public static String createJwtAuthentication(String userInfo) {
        return Jwts.builder()
                .claim("authorities", "role_admin,user_write")
                .setSubject(userInfo)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public static Authentication getAuthentication(HttpServletRequest request) {
        Claims claims = Jwts.parser()
                // 验签
                .setSigningKey(SECRET)
                // 去掉 Basic
                .parseClaimsJws(request.getHeader(REQUEST_HEADER).replace(TOKEN_PREFIX, ""))
                .getBody();
        String userInfo = claims.getSubject();
        List<GrantedAuthority> authorities =  AuthorityUtils.commaSeparatedStringToAuthorityList((String) claims.get("authorities"));
        // 返回验证令牌
        return userInfo != null ?
                new UsernamePasswordAuthenticationToken(userInfo, null, authorities) :
                null;
    }
}
