package DoAnJava.LinhKienDienTu.utils;

import DoAnJava.LinhKienDienTu.entity.CustomOAuth2User;
import DoAnJava.LinhKienDienTu.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuthLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        CustomOAuth2User oauth2User = (CustomOAuth2User) authentication.getPrincipal();
        String oauth2ClientName = oauth2User.getOAuth2ClientName();
        String username = oauth2User.getName();
        String email = oauth2User.getEmail();
        String fullname = oauth2User.getFullName();

        userService.updateAuthenticationType(username, email, fullname, oauth2ClientName);

        super.onAuthenticationSuccess(request, response, authentication);

    }
}
