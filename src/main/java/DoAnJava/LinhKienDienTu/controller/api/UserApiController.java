package DoAnJava.LinhKienDienTu.controller.api;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserApiController {
    @GetMapping("/check-authentication")
    public Map<String, Boolean> checkAuthentication(Authentication authentication) {
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated();
        return Collections.singletonMap("authenticated", isAuthenticated);
    }
}
