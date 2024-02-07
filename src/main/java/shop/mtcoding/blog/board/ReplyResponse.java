package shop.mtcoding.blog.board;

import lombok.AllArgsConstructor;
import lombok.Data;

public class ReplyResponse {

    @AllArgsConstructor
    @Data
    public static class ReplyDTO{
        private String comment;
    }

    @AllArgsConstructor
    @Data
    public static class ReplyDetailDTO{
        private String username;
        private String comment;
    }
}
