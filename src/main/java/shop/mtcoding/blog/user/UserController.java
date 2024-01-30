package shop.mtcoding.blog.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserRepository userRepository;
    private final HttpSession session;


    @PostMapping("/login")
    public String login(UserRequest.LoginDTO requestDTO){
        // 1. 유효성 검사
        if(requestDTO.getUsername().length()<3){
            return "error/400";
        }

        // 2. Model 필요
        User user = userRepository.findByUsernameAndPassword(requestDTO);
        if(user == null){
            return "error/401";
        }else{
            session.setAttribute("sessionUser",user);
            return "redirect:/";
        }
    }

    @PostMapping("/join")
    public String join(UserRequest.JoinDTO requestDTO) {
        System.out.println(requestDTO);

        // 1. 유효성 검사
        if(requestDTO.getUsername().length()<3){
            return "error/400";
        }

        // 2. Model에 위임하기
        userRepository.saveV2(requestDTO);

        // 3. 응답
        return "redirect:/loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "user/joinForm";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "user/loginForm";
    }

    @GetMapping("/user/updateForm")
    public String updateForm() {
        return "user/updateForm";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }
}
