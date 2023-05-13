package DoAnJava.LinhKienDienTu.entity;

import DoAnJava.LinhKienDienTu.validator.annotation.ValidEmail;
import DoAnJava.LinhKienDienTu.validator.annotation.ValidUsername;
import jakarta.persistence.*;
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

    @Column(name = "fullname", length = 64, nullable = false)
    @Size(max = 64, message = "Your name must be less than 64 characters")
    @NotBlank(message = "Your name is required")
    private String fullname;

    @Column(name = "phone", length = 10)
    private String phone;

    @Email(message = "Email invalid")
    @Column(name = "email", length = 144, nullable = false, unique = true)
    @NotBlank(message = "Email is required")
    @ValidEmail
    private String email;

    @Column(name = "username", length = 64, nullable = false, unique = true)
    @NotBlank(message = "Username is required")
    @Size(min = 6, max = 64, message = "Username must be more than 6 characters")
    @ValidUsername
    private String username;

    @Column(name = "password", length = 144, nullable = false)
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be more than 6 characters")
    private String password;

    @Column(name = "confirm_password", nullable = false)
    private String confirmPassword;

    @Column(name = "verifycation_code", length = 64)
    private String verificationCode;

    private boolean enabled;
}
