package DoAnJava.LinhKienDienTu.entity;

import DoAnJava.LinhKienDienTu.validator.annotation.ValidEmail;
import DoAnJava.LinhKienDienTu.validator.annotation.ValidUsername;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
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

    @Column(name = "verifycation_code", length = 64)
    private String verificationCode;

    @Column(name = "reset_password_token")
    private String resetPasswordToken;

    private boolean enabled;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Bill bill;

    @OneToMany(mappedBy = "user")
    private Set<Comment> comments;
}
