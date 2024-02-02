package shop.mtcoding.blog.board;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import shop.mtcoding.blog.user.User;

import java.time.LocalDateTime;

@Data // getter, setter, toString
@Entity
@Table(name = "board_tb")
public class Board {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private int id;
    private String title;
    private String content;

    @ManyToOne // 1:N 의미 foreign key를 만들어줌
    private User user;

    @CreationTimestamp
    private LocalDateTime createdAt;
}