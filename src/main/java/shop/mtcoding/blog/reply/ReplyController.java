package shop.mtcoding.blog.reply;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

// 댓글쓰기, 댓글삭제, 댓글 목록보기
@RequiredArgsConstructor
@Controller
public class ReplyController {

    @PostMapping("/reply/save")
    public void write(){

    }
}
