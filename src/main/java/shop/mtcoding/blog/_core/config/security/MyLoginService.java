package shop.mtcoding.blog._core.config.security;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import shop.mtcoding.blog.user.User;
import shop.mtcoding.blog.user.UserRepository;

// POST, /login, x-www-form-urlencoded, 키값이 username, password
@RequiredArgsConstructor
@Service
public class MyLoginService implements UserDetailsService { // SpringSecurity 라이르러리를 IOC에 달면 UserDetailsService가 등록이 최초 실행될때 되어있음. 그걸 무력화

    private final UserRepository userRepository;
    private final HttpSession session;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("username = " + username);
        User user = userRepository.findByUsername(username);

        if(user == null){
            System.out.println("user는 null");
            return null;
        }else{
            System.out.println("user를 찾았어요");
            session.setAttribute("sessionUser",user); // 머스테치에서만 가져오자
            return new MyLoginUser(user); // 세션에 넣기 직전에 getPasswrod를 때려서 return과 User객체와 비교해서 맞으면 세션 생성
            // SecurityContextHolder에 저장
        }

    }
}
