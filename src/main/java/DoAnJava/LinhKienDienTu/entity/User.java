package DoAnJava.LinhKienDienTu.entity;

import DoAnJava.LinhKienDienTu.validator.annotation.ValidEmail;
import DoAnJava.LinhKienDienTu.validator.annotation.ValidUsername;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue
    private UUID userId;

    @Column(name = "fullname", length = 40)
    @Size(max = 60, message = "Tên của bạn không được vượt quá 60 ký tự.")
    private String fullname;

    @Column(name = "phone", length = 10)
    @Pattern(regexp = "^[0-9]{10}$", message = "Số điện thoại phải gồm 10 chữ số.")
    private String phone;

    @Email(message = "Email invalid")
    @Column(name = "email", length = 40)
    @ValidEmail
    private String email;

    @Column(name = "username", length = 40)
    @Size(min = 5, message = "Tên tài khoản phải ít nhất 5 ký tự.")
    @Size(max = 40, message = "Tài khoản không được vượt quá 40 ký tự.")
    @ValidUsername
    private String username;

    @Column(name = "password", length = 64)
    @Size(min = 6, message = "Mật khẩu phải ít nhất 6 ký tự.")
    @Size(max = 144, message = "Mật khẩu không được vượt quá 144 ký tự.")
    private String password;

    @Column(name = "verifycation_code", length = 64)
    private String verificationCode;

    @Column(name = "reset_password_token", length = 30)
    private String resetPasswordToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_type", length = 15)
    private AuthenticationType authType;

    private boolean enabled;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Bill> bill;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Comment> comments;
}
