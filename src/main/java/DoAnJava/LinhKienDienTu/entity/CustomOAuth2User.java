package DoAnJava.LinhKienDienTu.entity;

import DoAnJava.LinhKienDienTu.repository.IUserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

public class CustomOAuth2User implements OAuth2User {
    private final OAuth2User oAuth2User;

    private final IUserRepository userRepository;

    public CustomOAuth2User(OAuth2User oAuth2User, IUserRepository userRepository) {
        this.oAuth2User = oAuth2User;
        this.userRepository = userRepository;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        String username = oAuth2User.getAttribute("email");
        String[] roles = userRepository.getRolesOfUser(username);
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }

    @Override
    public String getName() {
        return oAuth2User.getAttribute("email");
    }

    public String getFullName() {
        return oAuth2User.<String>getAttribute("name");
    }
}
