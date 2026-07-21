package BridgeLabz.Book_Store_Application.security;

import BridgeLabz.Book_Store_Application.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority(user.getRole().name())
        );
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Spring Security uses this as the username.
     * In our project, the email is the username.
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * Account expired?
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Account locked?
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Password expired?
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * User enabled?
     */
    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(user.getActive());
    }

    public Long getId() {
        return user.getId();
    }
}