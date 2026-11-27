package org.secretfriend.config.security;

import lombok.NonNull;
import org.secretfriend.model.document.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public record CustomUserDetails(

        User user

) implements UserDetails {

    public String getUserId() { return user.getId(); }

    @Override
    @NonNull
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    @NonNull
    public Collection<? extends GrantedAuthority> getAuthorities() { return Collections.emptyList(); }
}
