package shop.mtcoding.blog.user;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 컨트롤러
 * 1. 요청받기 (URL - URI 포함)
 * 2. http body는 DTO로 받기
 * 3. 기본 Mime 전략 : x-www-form-urlencoded (username=ssar&password=1234)
 * 4. 유효성 검사하기 (body 데이터가 있다면)
 * 5. 클라이언트가 View만 원하는지? 혹은 DB처리 후 View도 원하는지?
 * 6. View만 원하면 View를 응답하면 끝
 * 7. DB처리를 원하면 Model(DAO)에게 위임 후 View를 응답하면 끝
 */

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserRepository userRepository;
    // IoC 컨테이너에 세션에 접근할 수 있는 변수가 들어가 있음
    private final HttpSession session;

    // 로그인만 예외로 POST한다
    @PostMapping("/login")
    public String login(UserRequest.LoginDTO requestDTO) {
        // 1. 유효성 검사
        if (requestDTO.getUsername().length() < 3) {
            throw new RuntimeException("유저네임의 길이는 3자 이상이어야 합니다.");
        }

        // 2. Model 필요 (select * from user_tb where username=? and password=?)
        User user = userRepository.findByUsername(requestDTO.getUsername());

        // password 검증 실패
        if (!BCrypt.checkpw(requestDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("패스워드가 틀렸습니다.");
        }
        session.setAttribute("sessionUser", user);

        return "redirect:/";
    }

    @PostMapping("/join")
    public String join(UserRequest.JoinDTO requestDTO) {
        // @ResponseBody를 적으면 파일을 반환하는것이 아니라 메시지를 반환한다.
        System.out.println(requestDTO);

        String rawPassword = requestDTO.getPassword();
        String encPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt());
        requestDTO.setPassword(encPassword);

        // 1. 유효성 검사
        if (requestDTO.getUsername().length() < 3) {
            return "error/400";
        }

        // 2.동일 username 체크 (나중에 하나의 트랜잭션으로 묶는게 좋다.)
        User user = userRepository.findByUsername(requestDTO.getUsername());
        try {
            userRepository.save(requestDTO);
        } catch (Exception e) {
            throw new RuntimeException("아이디가 중복되었어요");
        }

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

    @PostMapping("/user/update")
    public String update(UserRequest.UpdateDTO requestDTO) {
        // 인증 체크, 권한 체크
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }

        // 핵심 로직
        userRepository.update(requestDTO, sessionUser.getId());

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout() {
        session.invalidate();
        return "redirect:/";
    }
}
