package shop.mtcoding.blog.board;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.blog._core.Constant;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class BoardRepository {
    private final EntityManager em;
    private final HttpSession session;

    @Transactional
    public void update(int boardId, BoardRequest.UpdateDTO requestDTO){
        Query query = em.createNativeQuery("update board_tb set title=?, content=? where id = ?");
        query.setParameter(1,requestDTO.getTitle());
        query.setParameter(2,requestDTO.getContent());
        query.setParameter(3,boardId);
        query.executeUpdate();
    }

    @Transactional
    public void delete(int boardId) {
        Query query = em.createNativeQuery("delete from board_tb where id = ?");
        query.setParameter(1, boardId);
        query.executeUpdate();

    }

    public int count() {
        Query query = em.createNativeQuery("select count(*) from board_tb");

//        int totalCount = (Integer) query.getSingleResult(); 이거 안됨;
        int totalCount = ((Number) query.getSingleResult()).intValue();
//        System.out.println("총 게시글" + totalCount);
        return totalCount;
    }

    // 페이징 과정
    public int count(String title){
        Query query = em.createNativeQuery("select count(*) from board_tb where title like ? ");
        query.setParameter(1,"%"+title+"%");

        int searchCount = ((Number) query.getSingleResult()).intValue();

        return searchCount;
    }

    public List<Board> findAll(String title, int page) {
        int value = Constant.PAGING_COUNT * page;
        Query query = em.createNativeQuery("select * from board_tb where title like ? order by id desc limit ?,?", Board.class);
        query.setParameter(1, "%" + title + "%");
        query.setParameter(2, value);
        query.setParameter(3, Constant.PAGING_COUNT);

        List<Board> boardList = query.getResultList();

        return boardList;

    }

    public List<Board> findAll(int page) {
        int value = Constant.PAGING_COUNT * page;
        Query query = em.createNativeQuery("select * from board_tb order by id desc limit ?,?", Board.class);

        query.setParameter(1, value);
        query.setParameter(2, Constant.PAGING_COUNT);

        List<Board> boardList = query.getResultList(); // 결과가 여러건이기 때문에 ResultList
        return boardList;
    }

    public Board findById(int id){
        Query query = em.createNativeQuery("select * from board_tb where id = ?", Board.class);
        query.setParameter(1, id);

        Board board = (Board) query.getSingleResult();
        return board;
    }

    public BoardResponse.DetailDTO findByIdWithUser(int idx) {
        Query query = em.createNativeQuery("select b.id, b.title, b.content, b.user_id, u.username from board_tb b inner join user_tb u on b.user_id = u.id where b.id = ?");
        query.setParameter(1, idx);

        Object[] row = (Object[]) query.getSingleResult();

        Integer id = (Integer) row[0];
        String title = (String) row[1];
        String content = (String) row[2];
        Integer userId = (Integer) row[3];
        String username = (String) row[4];

        System.out.println("id : " + id);
        System.out.println("title : " + title);
        System.out.println("content : " + content);
        System.out.println("userId : " + userId);
        System.out.println("username : " + username);

        BoardResponse.DetailDTO responseDTO = new BoardResponse.DetailDTO();
        responseDTO.setId(id);
        responseDTO.setTitle(title);
        responseDTO.setContent(content);
        responseDTO.setUserId(userId);
        responseDTO.setUsername(username);

        return responseDTO;
    }

//    public BoardResponse.DetailDTO findByIdWithUser(int id) {
//        // Entity가 아닌 것은 JPA가 파싱안해준다. (Join을 해서 Entity가 아님)
//        Query query = em.createNativeQuery("select bt.id, bt.title, bt.content, bt.user_id, ut.username from " +
//                "board_tb bt inner join user_tb ut " +
//                "on bt.user_id = ut.id where bt.id=?");
//        query.setParameter(1, id);
//
//        JpaResultMapper rm = new JpaResultMapper(); // qlrm
//

//
//        BoardResponse.DetailDTO responseDTO = rm.uniqueResult(query, BoardResponse.DetailDTO.class);
//        return responseDTO;
//    }

    @Transactional
    public void save(BoardRequest.SaveDTO saveDTO, int userId) {

        Query query = em.createNativeQuery("insert into board_tb(title,content,user_id) values (?,?,?)");

        query.setParameter(1, saveDTO.getTitle());
        query.setParameter(2, saveDTO.getContent());
        query.setParameter(3, userId);

        query.executeUpdate();
    }


}
