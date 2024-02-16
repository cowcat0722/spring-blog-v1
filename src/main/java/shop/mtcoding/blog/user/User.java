package shop.mtcoding.blog.user;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user_tb")
public class User {
    @Id // javax의 @Id -> Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AutoIncrement
    private Integer id;

    @Column(unique = true) // username을 unique로
    private String username;

    @Column(length = 60, nullable = false) // 60자 까지만, null일수 없다
    private String password;
    private String email;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
