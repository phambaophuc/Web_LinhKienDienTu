package DoAnJava.LinhKienDienTu.services;

import DoAnJava.LinhKienDienTu.entity.CustomOAuth2User;
import DoAnJava.LinhKienDienTu.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private IUserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user =  super.loadUser(userRequest);
        if (user == null) throw new OAuth2AuthenticationException("Invalid user");
        return new CustomOAuth2User(user, userRepository);
    }
}
