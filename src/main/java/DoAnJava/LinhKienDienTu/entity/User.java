package DoAnJava.LinhKienDienTu.entity;

import DoAnJava.LinhKienDienTu.validator.annotation.ValidEmail;
import DoAnJava.LinhKienDienTu.validator.annotation.ValidUsername;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue
    private UUID userId;

    @Column(name = "fullname", length = 60, nullable = false)
    @Size(min = 4, max = 60, message = "Tên của bạn phải từ 4-60 ký tự.")
    private String fullname;

    @Column(name = "phone", length = 10)
    private String phone;

    @Email(message = "Email invalid")
    @Column(name = "email", length = 144, nullable = false, unique = true)
    @NotBlank(message = "Vui lòng nhập Email của bạn.")
    @ValidEmail
    private String email;

    @Column(name = "username", length = 40, nullable = false, unique = true)
    @Size(min = 5, max = 40, message = "Tài khoản của bạn phải từ 5-40 ký tự.")
    @ValidUsername
    private String username;

    @Column(name = "password", length = 144, nullable = false)
    @Size(min = 6, max = 144, message = "Mật khẩu của bạn phải từ 6-144 ký tự.")
    private String password;

    @Column(name = "confirm_password", length = 144, nullable = false)
    @NotBlank(message = "Vui lòng nhập mật khẩu xác nhận.")
    private String confirmPassword;

    @Column(name = "verifycation_code", length = 64)
    private String verificationCode;

    private boolean enabled;

    @AssertTrue(message = "Mật khẩu xác nhận phải giống mật khẩu.")
    public boolean isPasswordConfirmed() {
        return password != null && password.equals(confirmPassword);
    }
}
