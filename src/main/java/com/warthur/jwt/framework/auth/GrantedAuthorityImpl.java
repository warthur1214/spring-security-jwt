package com.warthur.jwt.framework.auth;

import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Setter
@AllArgsConstructor
public class GrantedAuthorityImpl implements GrantedAuthority {

    private String authority;


    @Override
    public String getAuthority() {
        return authority;
    }
}
