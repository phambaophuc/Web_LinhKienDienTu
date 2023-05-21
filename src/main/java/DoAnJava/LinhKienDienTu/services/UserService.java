package DoAnJava.LinhKienDienTu.services;

import DoAnJava.LinhKienDienTu.entity.User;
import DoAnJava.LinhKienDienTu.reponsitory.IRoleReponsitory;
import DoAnJava.LinhKienDienTu.reponsitory.IUserReponsitory;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private IUserReponsitory userReponsitory;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private IRoleReponsitory roleReponsitory;

    public List<User> getAllUsers() {
        return userReponsitory.findAll();
    }

    public User getUserByUsername(String username) {
        return userReponsitory.findByUsername(username);
    }

    public User getUserById(UUID id) {
        Optional<User> optionalUser = userReponsitory.findById(id);
        return optionalUser.orElse(null);
    }

    public void addRoleToUser(UUID userId, UUID roleId) {
        userReponsitory.addRoleToUser(userId, roleId);
    }

    public String[] getRolesOfUser(UUID id) {
        return userReponsitory.getRolesOfUser(id);
    }

    public void updateResetPasswordToken(String token, String email) throws UsernameNotFoundException {
        User user = userReponsitory.findByEmail(email);
        if (user != null) {
            user.setResetPasswordToken(token);
            userReponsitory.save(user);
        } else {
            throw new UsernameNotFoundException("Không tìm thấy user với email là: " + email);
        }
    }

    public User getByResetPasswordToken(String token) {
        return userReponsitory.findByResetPasswordToken(token);
    }

    public void updatePassword(User user, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);

        user.setResetPasswordToken(null);
        userReponsitory.save(user);
    }

    public void register(User user, String siteURL)
            throws UnsupportedEncodingException, MessagingException {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        String randomCode = RandomStringUtils.randomAlphanumeric(64);
        user.setVerificationCode(randomCode);
        user.setEnabled(false);

        userReponsitory.save(user);
        UUID userId = userReponsitory.getUserIdByUsername(user.getUsername());
        UUID roleId = roleReponsitory.getRoleIdByRoleName("USER");
        if (roleId != null && userId != null) {
            userReponsitory.addRoleToUser(userId, roleId);
        }

        sendVerificationEmail(user, siteURL);
    }

    private void sendVerificationEmail(User user, String siteURL)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "123phuc27602@gmail.com";
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

    public boolean verify(String verificationCode) {
        User user = userReponsitory.findByVerificationCode(verificationCode);

        if (user == null || user.isEnabled()) {
            return false;
        } else {
            user.setVerificationCode(null);
            user.setEnabled(true);
            userReponsitory.save(user);

            return true;
        }
    }
}
