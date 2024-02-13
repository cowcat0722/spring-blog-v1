package shop.mtcoding.blog.board;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shop.mtcoding.blog._core.config.security.MyLoginUser;
import shop.mtcoding.blog._core.util.PagingUtil;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BoardController {

    private final HttpSession session;
    private final BoardRepository boardRepository;

    @GetMapping("/search")
    public String search(HttpServletRequest request, @RequestParam(value = "title") String title, @RequestParam(defaultValue = "0") int page) {
        List<Board> boardList = boardRepository.findAll(title, page);
        request.setAttribute("boardList", boardList);

        request.setAttribute("search", title);

        int currentPage = page;
        int nextPage = currentPage + 1;
        int prevPage = currentPage - 1;
        request.setAttribute("nextPage", nextPage);
        request.setAttribute("prevPage", prevPage);

        boolean first = PagingUtil.isFirst(currentPage);
        int totalCount = boardRepository.count(title);
        boolean last = PagingUtil.isLast(currentPage, totalCount);

        request.setAttribute("first", first);
        request.setAttribute("last", last);

        return "index";
    }

    // http://localhost:8080?page=0
    @GetMapping({"/"})
    public String index(HttpServletRequest request, @RequestParam(defaultValue = "0") int page) {
        // 위임만 하면 끝
        List<Board> boardList = boardRepository.findAll(page);
        request.setAttribute("boardList", boardList);

        String title = "";
        request.setAttribute("search", title);

        int currentPage = page;
        int nextPage = currentPage + 1;
        int prevPage = currentPage - 1;
        request.setAttribute("nextPage", nextPage);
        request.setAttribute("prevPage", prevPage);

        boolean first = PagingUtil.isFirst(currentPage);
        int totalCount = boardRepository.count();
        boolean last = PagingUtil.isLast(currentPage, totalCount);

        request.setAttribute("first", first);
        request.setAttribute("last", last);

        return "index";
    }

    @GetMapping("/board/saveForm")
    public String saveForm() {
        return "board/saveForm";
    }

    @PostMapping("board/{id}/replySave")
    public String replySave(@PathVariable int id, ReplyResponse.ReplyDTO responseDTO, @AuthenticationPrincipal MyLoginUser myLoginUser) {
        Board board = boardRepository.findById(id);

        // DB에 댓글 등록
        boardRepository.replySave(board.getId(), myLoginUser.getUser().getId(), responseDTO);

        return "redirect:/board/{id}";
    }

    @GetMapping("/board/{id}")
    public String detail(@PathVariable int id, HttpServletRequest request, @AuthenticationPrincipal MyLoginUser myLoginUser) {
        BoardResponse.DetailDTO responseDTO = boardRepository.findByIdWithUser(id);
        request.setAttribute("board", responseDTO);
        // 화면상에 댓글 뿌리기
        Board board = boardRepository.findById(id);
        List<ReplyResponse.ReplyDetailDTO> replyDetailDTOList = boardRepository.findReplyByIdWithUser(board.getId());
        request.setAttribute("reply", replyDetailDTOList);

        // 1. 해당 페이지의 주인여부
        boolean owner = false;

        // 2. 작성자 userId 확인하기
        int boardUserId = responseDTO.getUserId();
        if(myLoginUser!=null) {
            if (boardUserId == myLoginUser.getUser().getId()) {
                owner = true;
            }
        }

        request.setAttribute("owner", owner);

        return "board/detail";
    }

    @PostMapping("/board/{id}/delete")
    public String delete(BoardRequest.DeleteDTO deleteDTO) {
        boardRepository.delete(deleteDTO.getId());

        return "redirect:/board";
    }

    @PostMapping("/board/save")
    public String save(BoardRequest.SaveDTO saveDTO, HttpServletRequest request, @AuthenticationPrincipal MyLoginUser myLoginUser) {

        if (saveDTO.getTitle().length() > 30) {
            request.setAttribute("status", 400);
            request.setAttribute("msg", "title의 길이가 30자를 초과해서는 안되요");
            return "error/40x"; // BadRequest
        }

        boardRepository.save(saveDTO, myLoginUser.getUser().getId());
        return "redirect:/";
    }

    @PostMapping("/board/{id}/update")
    public String update(@PathVariable int id, BoardRequest.UpdateDTO requestDTO, @AuthenticationPrincipal MyLoginUser myLoginUser) {
        // 2. 권한 체크
        Board board = boardRepository.findById(id);
        if (myLoginUser.getUser().getId() != board.getUserId()) {
            return "error/403";
        }

        // 3. 핵심 로직
        boardRepository.update(board.getId(), requestDTO);
        return "redirect:/board/{id}";
    }

    @GetMapping("/board/{id}/updateForm")
    public String updateForm(@PathVariable int id, HttpServletRequest request, @AuthenticationPrincipal MyLoginUser myLoginUser) {
        // 모델 위임 (id로 board를 조회)
        Board board = boardRepository.findById(id);

        // 권한 체크
        if (myLoginUser.getUser().getId() != board.getUserId()) {
            return "error/403";
        }

        // 3. 가방에 담기
        request.setAttribute("board", board);

        return "board/updateForm";
    }

}