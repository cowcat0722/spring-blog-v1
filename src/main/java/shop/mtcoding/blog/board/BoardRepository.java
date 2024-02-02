package shop.mtcoding.blog.board;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
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
}
