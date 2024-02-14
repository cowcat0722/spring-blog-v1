package shop.mtcoding.blog.reply;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data // getter, setter, toString
@Entity
@Table(name = "reply_tb")
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String comment;
    private int userId;
    private int boardId;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
