package DoAnJava.LinhKienDienTu.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue
    private UUID userId;

    @Column(name = "fullname", length = 64)
    private String fullname;

    @Column(name = "phone", length = 10)
    private String phone;

    @Column(name = "email", length = 144, nullable = false)
    private String email;

    @Column(name = "username", length = 64, nullable = false)
    private String username;

    @Column(name = "password", length = 64, nullable = false)
    private String password;
}
