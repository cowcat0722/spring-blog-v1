package shop.mtcoding.blog._core;

import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

public class BcryptTest {

    @Test
    public void login_test(){
        String joinPassword = "1234";
        String enc1Password = BCrypt.hashpw(joinPassword,BCrypt.gensalt());
        System.out.println("DB : "+enc1Password);

        String loginPassword = "1234";
        String enc2Password = BCrypt.hashpw(loginPassword,BCrypt.gensalt());
        System.out.println("DB : "+enc2Password);

        boolean test = BCrypt.checkpw(loginPassword,enc1Password);
        System.out.println(test);
    }

    @Test
    public void gensalt_test(){
        String salt = BCrypt.gensalt();
        System.out.println(salt);
    }

    @Test
    public void hashpw_test(){
        String rawPassword = "1234";
        String encPassword = BCrypt.hashpw(rawPassword,BCrypt.gensalt());
        System.out.println(encPassword);
    }
}
