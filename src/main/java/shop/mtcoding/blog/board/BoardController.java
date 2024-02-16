package shop.mtcoding.blog.board;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import shop.mtcoding.blog._core.PagingUtil;
import shop.mtcoding.blog.reply.Reply;
import shop.mtcoding.blog.reply.ReplyRepository;
import shop.mtcoding.blog.user.User;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BoardController {

    private final HttpSession session;
    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;



//    @GetMapping("/search")
//    public String search(HttpServletRequest request, @RequestParam(value = "title") String title, @RequestParam(defaultValue = "0") int page){
//        List<Board> boardList = boardRepository.findAll(title,page);
//        request.setAttribute("boardList",boardList);
//
//        request.setAttribute("search",title);
//
//
//        int currentPage = page;
//        int nextPage = currentPage+1;
//        int prevPage = currentPage-1;
//        request.setAttribute("nextPage",nextPage);
//        request.setAttribute("prevPage",prevPage);
//
//        boolean first = PagingUtil.isFirst(currentPage);
//        int totalCount = boardRepository.count(title);
//        boolean last = PagingUtil.isLast(currentPage,totalCount);
//
//        request.setAttribute("first",first);
//        request.setAttribute("last",last);
//
//        return "index";
//    }

    // http://localhost:8080?page=0
    @GetMapping("/")
    public String index(
            HttpServletRequest request,
            @RequestParam(value = "title", defaultValue = "") String title,
            @RequestParam(value = "page",defaultValue = "0") Integer page
    ) {
        // isEmpty -> Null, 공백
        // isBlank -> Null, 공백, 스페이스
        List<Board> boardList = null;
        int totalCount;
        if (title.isBlank()){
            boardList = boardRepository.findAll(page);
            totalCount = boardRepository.count();
        }else {
            boardList = boardRepository.findAll(title,page);
            totalCount = boardRepository.count(title);
        }
        request.setAttribute("boardList",boardList);
        request.setAttribute("search",title);

//        boolean searchThing = false;
//        request.setAttribute("search",searchThing);

        Integer currentPage = page;
        int nextPage = currentPage+1;
        int prevPage = currentPage-1;
        request.setAttribute("nextPage",nextPage);
        request.setAttribute("prevPage",prevPage);

        boolean first = PagingUtil.isFirst(currentPage);

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
        User sessionUser = (User) session.getAttribute("sessionUser");
        BoardResponse.DetailDTO boardDTO = boardRepository.findByIdWithUser(id);
        boardDTO.isBoardOwner(sessionUser);

        List<BoardResponse.ReplyDTO> replyDTOList = replyRepository.findByBoardId(id, sessionUser);

        request.setAttribute("board",boardDTO);
        request.setAttribute("replyList",replyDTOList);

        return "board/detail";
    }

    @PostMapping("/board/{id}/delete")
    public String delete(BoardRequest.DeleteDTO deleteDTO){
        // 인증 체크
        User sessionUser = (User) session.getAttribute("sessionUser");
        if(sessionUser == null){
            return "redirect:/loginForm";
        }

        boardRepository.delete(deleteDTO.getId());
        return "redirect:/";
    }

    @PostMapping("/board/save")
    public String save(BoardRequest.SaveDTO saveDTO, HttpServletRequest request) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        System.out.println(sessionUser);
        if(sessionUser == null){
            return "redirect:/loginForm";
        }

        if(saveDTO.getTitle().length()>30){
            request.setAttribute("status",400);
            request.setAttribute("msg","title의 길이가 30자를 초과해서는 안되요");
            return "error/40x"; // BadRequest
        }

        boardRepository.save(saveDTO,sessionUser.getId());
        return "redirect:/";
    }

    @PostMapping("/board/{id}/update")
    public String update(@PathVariable int id, BoardRequest.UpdateDTO requestDTO){
        // 1. 인증 체크
        User sessionUser = (User) session.getAttribute("sessionUser");
        if(sessionUser == null){
            return "redirect:/loginForm";
        }
        // 2. 권한 체크
        Board board = boardRepository.findById(id);
        if (sessionUser.getId() != board.getUserId()){
            return "error/403";
        }

        // 3. 핵심 로직
        boardRepository.update(board.getId(), requestDTO);
        return "redirect:/board/{id}";
    }

    @GetMapping("/board/{id}/updateForm")
    public String updateForm(@PathVariable int id, HttpServletRequest request){
        // 인증 체크
        User sessionUser = (User) session.getAttribute("sessionUser");
        if(sessionUser == null){
            return "redirect:/loginForm";
        }

        // 모델 위임 (id로 board를 조회)
        Board board = boardRepository.findById(id);

        // 권한 체크
        if (sessionUser.getId() != board.getUserId()){
            return "error/403";
        }

        // 3. 가방에 담기
        request.setAttribute("board",board);

        return "board/updateForm";
    }

    @PostMapping("/reply/{id}/delete")
    public String delete(@PathVariable int id){
        // 인증 검사
        User sessionUser = (User) session.getAttribute("sessionUser");

        if (sessionUser == null) {
            return "redirect:/loginForm";
        }

        // 권한 검사 (댓글이 없거나, 댓글 주인이거나, 댓글 주인이 아니거나)
        Reply reply = replyRepository.findById(id);

        if (reply == null){
            return "error/404";
        }

        if (reply.getUserId() != sessionUser.getId()){
            return "error/403";
        }

        // 핵심 로직
        replyRepository.deleteById(id);

        return "redirect:/board/"+reply.getBoardId();
    }
}