package com.warthur.jwt.framework.filter;

import com.alibaba.fastjson.JSON;
import com.warthur.jwt.common.util.Error;
import com.warthur.jwt.common.util.ResponseUtil;
import com.warthur.jwt.framework.auth.TokenAuthenticationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        Authentication authentication = null;
        try {
            authentication = TokenAuthenticationService
                    .getAuthentication((HttpServletRequest)servletRequest);
        } catch (Exception e) {
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            response.setHeader("Content-Type", "application/json;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().print(JSON.toJSONString(ResponseUtil.error(Error.ILLEGAL_TOKEN_ERROR)));
            return;
        }

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
        filterChain.doFilter(servletRequest,servletResponse);
    }
}
