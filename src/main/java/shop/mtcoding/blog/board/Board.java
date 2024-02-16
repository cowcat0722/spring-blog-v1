package shop.mtcoding.blog.board;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data // getter, setter, toString
@Entity
@Table(name = "board_tb")
public class Board {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String content;
    private Integer userId;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
