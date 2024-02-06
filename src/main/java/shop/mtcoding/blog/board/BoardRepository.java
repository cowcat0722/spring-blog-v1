package shop.mtcoding.blog.board;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.qlrm.mapper.JpaResultMapper;
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
//    public int searchCount(String title){
//        Query query = em.createNativeQuery("select count(*) from board_tb where title like ? ");
//        query.setParameter(1,"%"+title+"%");
//
//        int searchCount = ((Number) query.getSingleResult()).intValue();
//
//        return searchCount;
//    }

    public List<Board> findAll(String title) {
        Query query = em.createNativeQuery("select * from board_tb where title like ? order by id desc", Board.class);
        query.setParameter(1, "%" + title + "%");

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

    public BoardResponse.DetailDTO findById(int id) {
        // Entity가 아닌 것은 JPA가 파싱안해준다. (Join을 해서 Entity가 아님)
        Query query = em.createNativeQuery("select bt.id, bt.title, bt.content, bt.created_at, bt.user_id, ut.username from " +
                "board_tb bt inner join user_tb ut " +
                "on bt.user_id = ut.id where bt.id=?");
        query.setParameter(1, id);

        JpaResultMapper rm = new JpaResultMapper(); // qlrm

        // 기본기
//        Object[] row = (Object[]) query.getSingleResult();
//
//        Integer bid = (Integer) row[0];
//        String title = (String) row[1];
//        String content = (String) row[2];
//        Timestamp created_at = (Timestamp) row[3];
//        Integer utid = (Integer) row[4];
//        String username = (String) row[5];

        BoardResponse.DetailDTO responseDTO = rm.uniqueResult(query, BoardResponse.DetailDTO.class);
        return responseDTO;
    }

    @Transactional
    public void save(BoardRequest.SaveDTO saveDTO, int userId) {

        Query query = em.createNativeQuery("insert into board_tb(title,content,user_id) values (?,?,?)");

        query.setParameter(1, saveDTO.getTitle());
        query.setParameter(2, saveDTO.getContent());
        query.setParameter(3, userId);

        query.executeUpdate();
    }

}
