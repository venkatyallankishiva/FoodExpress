package registerationlogin.security;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

public class CustomOidcUser implements OidcUser {
    private final Collection<? extends GrantedAuthority> authorities;
    private final OidcUser oidcUser;

    public CustomOidcUser(Collection<? extends GrantedAuthority> authorities, OidcUser oidcUser) {
        this.authorities = authorities;
        this.oidcUser = oidcUser;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oidcUser.getAttributes(); // ✅ Fix: Implement getAttributes()
    }

    @Override
    public Map<String, Object> getClaims() {
        return oidcUser.getClaims();
    }

    @Override
    public OidcIdToken getIdToken() {
        return oidcUser.getIdToken();
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return oidcUser.getUserInfo();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return oidcUser.getName();
    }
}
