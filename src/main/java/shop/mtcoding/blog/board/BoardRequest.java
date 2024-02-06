package shop.mtcoding.blog.board;

import lombok.AllArgsConstructor;
import lombok.Data;

public class BoardRequest {
    @AllArgsConstructor
    @Data
    public static class SaveDTO{
        private String title;
        private String content;
    }

    @AllArgsConstructor
    @Data
    public static class DeleteDTO{
        private int id;
    }
}
