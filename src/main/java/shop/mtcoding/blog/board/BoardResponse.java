package shop.mtcoding.blog.board;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

public class BoardResponse {

    @AllArgsConstructor
    @Data
    public static class DetailDTO{
        private int id;
        private String title;
        private String content;
        private Timestamp createdAt;
        private int userId;
        private String username;

    }
}
