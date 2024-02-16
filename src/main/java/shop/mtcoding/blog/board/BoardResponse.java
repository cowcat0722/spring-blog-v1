package shop.mtcoding.blog.board;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import shop.mtcoding.blog.user.User;

public class BoardResponse {

    // bt.id, bt.content, bt.title, bt.created_at, bt.user_id, ut.username
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class DetailDTO{
        private Integer id;
        private String title;
        private String content;
        private Integer userId; // 게시글 작성자 아이디
        private String username;
        private Boolean boardOwner;

        public void isBoardOwner(User sessionUser){
            if(sessionUser == null){
                boardOwner = false;
            }else{
                boardOwner = sessionUser.getId() == userId;
            }
        }
    }


    @Data
    public static class ReplyDTO{
        private Integer Id;
        private Integer userId;
        private String username;
        private String comment;
        private Boolean replyOwner; // 게시글 주인 여부 (세션값과 비교)

        public ReplyDTO(Object[] ob, User sessionUser) {
            this.Id = (Integer) ob[0];
            this.userId = (Integer) ob[1];
            this.username = (String) ob[2];
            this.comment = (String) ob[3];

            if (sessionUser == null){
                replyOwner = false;
            }else {
                replyOwner = sessionUser.getId() == userId;
            }
        }
    }
}
