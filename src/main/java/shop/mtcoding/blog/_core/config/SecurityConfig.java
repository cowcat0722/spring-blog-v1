package shop.mtcoding.blog._core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

@Configuration // 컴포넌트 스캔
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer ignore(){ // 정적자원 security filter에서 제외시키기
        return w -> w.ignoring().requestMatchers("/static/**", "/h2-console/**");
    }

    @Bean
    SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http.csrf(c -> c.disable());

        http.authorizeHttpRequests(a -> {
            a.requestMatchers(RegexRequestMatcher.regexMatcher("/board/\\d+")).permitAll()
                    .requestMatchers("/user/**", "/board/**").authenticated().anyRequest().permitAll();
        });

        // 세큐리티 로그인 페이지를 내가 만든 페이지로 설정
        http.formLogin(f -> {
            f.loginPage("/loginForm").loginProcessingUrl("/login").defaultSuccessUrl("/").failureUrl("/loginForm");
        });
        return http.build();
    }
}
