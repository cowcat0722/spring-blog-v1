package shop.mtcoding.blog.user;

import lombok.Data;

/**
 *  요청 DTO = Data Transfer Object (통신을 통해 Client로 부터 데이터를 전달받는 오브젝트)
 */
public class UserRequest {

    @Data // 롬복 Data = Getter + Setter + toString
    public static class JoinDTO{
        private String username;
        private String password;
        private String email;
    }

    @Data
    public static class LoginDTO{
        private String username;
        private String password;
    }
}
