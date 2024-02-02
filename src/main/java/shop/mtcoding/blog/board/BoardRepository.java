package shop.mtcoding.blog.board;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Repository;
import shop.mtcoding.blog._core.Constant;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class BoardRepository {
    private final EntityManager em;

    public List<Board> findAll(int page){ // 게시글들 전부 찾기
        int value = Constant.PAGING_COUNT*page;
        Query query = em.createNativeQuery("select * from board_tb order by id desc limit ?,?", Board.class);

        query.setParameter(1,value);
        query.setParameter(2,Constant.PAGING_COUNT);

        List<Board> boardList = query.getResultList(); // 결과가 여러건이기 때문에 ReusltList 하나면 SingleResult
        return boardList;
    }

    public int count() {
        Query query = em.createNativeQuery("select count(*) from board_tb");
        int totalCount = ((Number) query.getSingleResult()).intValue();

        return totalCount;
    }

    public void findById(int id) {
        //Entity가 아닌 것은 JPA가 파싱안해준다. (Join을 해서 Entity가 아님)
        Query query = em.createNativeQuery("select bt.id, bt.content, bt.title, bt.created_at, bt.user_id, ut.username " +
                "from board_tb bt inner join user_tb ut on bt.user_id = ut.id where bt.id=?");
        query.setParameter(1,id);

        JpaResultMapper rm = new JpaResultMapper();
        DetailDTO detailDTO = rm.uniqueResult(query. DetailDTO.class);
    }
}
