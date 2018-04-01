package com.warthur.jwt.framework.filter;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warthur.jwt.common.util.Constants;
import com.warthur.jwt.common.util.Error;
import com.warthur.jwt.common.util.ResponseUtil;
import com.warthur.jwt.framework.auth.AccountCredentials;
import com.warthur.jwt.framework.auth.TokenAuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public JwtLoginFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher(defaultFilterProcessesUrl));
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest,
                                                HttpServletResponse httpServletResponse)
            throws AuthenticationException, IOException {
        AccountCredentials credentials = new ObjectMapper().readValue(httpServletRequest.getInputStream(),
                AccountCredentials.class);

        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword()));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException {
        Object jwtToken = redisTemplate.opsForValue().get(Constants.AUTH_TOKEN_NAMESPACE + authResult.getName());
        if (jwtToken == null || jwtToken.equals("")) {
            jwtToken = TokenAuthenticationService.createJwtAuthentication(authResult.getName());
        }

        log.info("{}登录,token是：{}", authResult.getName(), jwtToken);
        response.setHeader("Content-Type", "application/json;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().print(JSON.toJSONString(ResponseUtil.success(jwtToken)));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        response.setHeader("Content-Type", "application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().print(JSON.toJSONString(ResponseUtil.error(Error.REQUEST_ERROR)));
    }
}
