package DoAnJava.LinhKienDienTu.services;

import DoAnJava.LinhKienDienTu.entity.AuthenticationType;
import DoAnJava.LinhKienDienTu.entity.User;
import DoAnJava.LinhKienDienTu.repository.IRoleRepository;
import DoAnJava.LinhKienDienTu.repository.IUserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private IRoleRepository roleRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User getUserById(UUID id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.orElse(null);
    }

    public void addRoleToUser(UUID userId, UUID roleId) {
        userRepository.addRoleToUser(userId, roleId);
    }

    public void removeRoleFromUser(UUID userId, UUID roleId) {
        userRepository.removeRoleFromUser(userId, roleId);
    }

    public String[] getRolesOfUser(UUID id) {
        return userRepository.getRolesOfUser(id);
    }

    public String[] getRolesOfRole(String username) {
        return userRepository.getRolesOfUser(username);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void updateResetPasswordToken(String token, String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setResetPasswordToken(token);
            userRepository.save(user);
        } else {
            throw new UsernameNotFoundException("Không tìm thấy user với email là: " + email);
        }
    }

    public User getByResetPasswordToken(String token) {
        return userRepository.findByResetPasswordToken(token);
    }

    public void updatePassword(User user, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);

        user.setResetPasswordToken(null);
        userRepository.save(user);
    }

    public void register(User user, String siteURL)
            throws UnsupportedEncodingException, MessagingException {

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        String randomCode = RandomStringUtils.randomAlphanumeric(64);
        user.setVerificationCode(randomCode);
        user.setEnabled(false);
        user.setAuthType(AuthenticationType.LOCAL);

        userRepository.save(user);
        UUID userId = userRepository.getUserIdByUsername(user.getUsername());
        UUID roleId = roleRepository.getRoleIdByRoleName("USER");
        if (roleId != null && userId != null) {
            userRepository.addRoleToUser(userId, roleId);
        }

        sendVerificationEmail(user, siteURL);
    }

    public boolean verify(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode);

        if (user == null || user.isEnabled()) {
            return false;
        } else {
            user.setVerificationCode(null);
            user.setEnabled(true);
            userRepository.save(user);

            return true;
        }
    }

    //region SendEmail
    private void sendVerificationEmail(User user, String siteURL)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = fromEmail;
        String senderName = "Phạm Bảo Phúc";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Your company name.";
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getFullname());
        String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);
    }

    public void sendForgotPasswordEmail(String recipientEmail, String link)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromEmail, "Support");
        helper.setTo(recipientEmail);

        String subject = "Here's the link to reset your password";
        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";

        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }
    //endregion

    public void updateAuthenticationType(String username, String email, String fullname, String oauth2ClientName) {
        User existUser = userRepository.findByUsername(username);
        AuthenticationType authType = AuthenticationType.valueOf(oauth2ClientName.toUpperCase());

        if (existUser == null) {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setEmail(email);
            newUser.setFullname(fullname);
            newUser.setEnabled(true);

            userRepository.save(newUser);
            userRepository.updateAuthenticationType(username, authType);

            UUID userId = userRepository.getUserIdByUsername(username);
            UUID roleId = roleRepository.getRoleIdByRoleName("USER");
            if (roleId != null && userId != null) {
                userRepository.addRoleToUser(userId, roleId);
            }
        } else {
            userRepository.updateAuthenticationType(username, authType);
        }
    }
}
