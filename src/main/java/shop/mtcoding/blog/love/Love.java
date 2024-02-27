package shop.mtcoding.blog.love;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;


@Table(name = "love_tb", uniqueConstraints = {
        @UniqueConstraint(
                name = "love_uk",
                columnNames = {"board_id", "user_id"}
        )})
@Data // getter, setter, toString
@Entity
public class Love {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer board_id;
    private Integer user_id;
    private Timestamp createdAt;
}
