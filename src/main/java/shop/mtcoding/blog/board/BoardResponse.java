package shop.mtcoding.blog.board;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

public class BoardResponse {

    // bt.id, bt.content, bt.title, bt.created_at, bt.user_id, ut.username
    @AllArgsConstructor
    @Data
    public static class DetailDTO{
        private Integer id;
        private String title;
        private String content;
        private Timestamp createdAt;
        private Integer userId;
        private String username;
    }

    // 만들필요 없었던거 같음
//    @AllArgsConstructor
//    @Data
//    public static class SaveDTO{
//        private Integer id;
//        private String title;
//        private String content;
//        private Integer userId;
//    }
}
