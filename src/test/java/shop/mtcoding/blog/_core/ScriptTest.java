package shop.mtcoding.blog._core;

import org.junit.jupiter.api.Test;
import shop.mtcoding.blog._core.util.Script;

class ScriptTest {

    @Test
    public void back_test(){
        String result = Script.back("안녕");
        System.out.println(result);
    }
}