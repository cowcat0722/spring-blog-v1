package shop.mtcoding.blog.board;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.blog._core.PagingUtil;
import shop.mtcoding.blog.user.User;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BoardController {

    private final HttpSession session;
    private final BoardRepository boardRepository;

    @GetMapping("/search")
    public String search(HttpServletRequest request, @RequestParam("title") String title){
        List<Board> boardList = boardRepository.findAll(title);
        request.setAttribute("boardList",boardList);

        return "index";
    }

    // http://localhost:8080?page=0
    @GetMapping({ "/", "/board" })
    public String index(HttpServletRequest request, @RequestParam(defaultValue = "0") int page) {
        // 위임만 하면 끝
        List<Board> boardList = boardRepository.findAll(page);
        request.setAttribute("boardList",boardList);

        int currentPage = page;
        int nextPage = currentPage+1;
        int prevPage = currentPage-1;
        request.setAttribute("nextPage",nextPage);
        request.setAttribute("prevPage",prevPage);

        boolean first = PagingUtil.isFirst(currentPage);
        int totalCount = boardRepository.count();
        boolean last = PagingUtil.isLast(currentPage,totalCount);

        request.setAttribute("first",first);
        request.setAttribute("last",last);

        return "index";
    }

    @GetMapping("/board/saveForm")
    public String saveForm() {
        // session 영역에 sessionUser 키값에 user 객체 있는지 체크
        User sessionUser = (User) session.getAttribute("sessionUser");

        // 값이 null 이면 로그인 페이지로 리다이렉션
        if(sessionUser == null){
            return "redirect:/loginForm";
        }
        // 값이 null 이 아니면, /board/saveForm 으로 이동
        return "board/saveForm";
    }

    @GetMapping("/board/{id}")
    public String detail(@PathVariable int id, HttpServletRequest request) {

        BoardResponse.DetailDTO responseDTO = boardRepository.findById(id);
        request.setAttribute("board",responseDTO);


        // 1. 해당 페이지의 주인여부
        boolean owner = false;

        // 2. 작성자 userId 확인하기
        int boardUserId = responseDTO.getUserId();

        // 3. 로그인 여부 체크
        User sessionUser = (User) session.getAttribute("sessionUser");
        if(sessionUser != null){ // 로그인 했고
            if(boardUserId == sessionUser.getId()){
                owner = true;
            }
        }
        
        request.setAttribute("owner",owner);

        return "board/detail";
    }

    @PostMapping("/board/save")
    public String save(BoardRequest.SaveDTO saveDTO) {
        boardRepository.save(saveDTO);
        return "redirect:/";
    }

    @PostMapping("/board/delete")
    public String delete(BoardResponse.DetailDTO detailDTO){
        boardRepository.delete(detailDTO);
        return "redirect:/board";
    }




}