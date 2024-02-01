package shop.mtcoding.blog.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import shop.mtcoding.blog._core.Constant;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class BoardRepository {
    private final EntityManager em;

    public int count(){
        Query query = em.createNativeQuery("select count(*) from board_tb");

//        int totalCount = (Integer) query.getSingleResult(); 이거 안됨;
        int totalCount = ((Number) query.getSingleResult()).intValue();
//        System.out.println("총 게시글" + totalCount);
        return totalCount;
    }

    public List<Board> findAll(int page){
        int value = Constant.PAGING_COUNT *page;
        Query query = em.createNativeQuery("select * from board_tb order by id desc limit ?,?", Board.class);

        query.setParameter(1,value);
        query.setParameter(2,Constant.PAGING_COUNT);

        List<Board> boardList = query.getResultList(); // 결과가 여러건이기 때문에 ResultList
        return boardList;
    }
}