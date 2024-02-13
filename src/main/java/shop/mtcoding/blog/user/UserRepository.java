package shop.mtcoding.blog.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository // 컴포넌트 스캔
public class UserRepository {

    private EntityManager em; // 의존성 주입

    public UserRepository(EntityManager em) {
        this.em = em;
    }

    @Transactional
    public void save(UserRequest.JoinDTO requestDTO){
        Query query = em.createNativeQuery("insert into user_tb(username,password,email) values (?,?,?)");
        query.setParameter(1,requestDTO.getUsername());
        query.setParameter(2,requestDTO.getPassword());
        query.setParameter(3,requestDTO.getEmail());

        query.executeUpdate();
    }

    @Transactional
    public void saveV2(UserRequest.JoinDTO requestDTO){
        User user = new User();
        user.setUsername(requestDTO.getUsername());
        user.setPassword(requestDTO.getPassword());
        user.setEmail(requestDTO.getEmail());

        em.persist(user);
    }

    public User findByUsernameAndPassword(UserRequest.LoginDTO requestDTO) {
        Query query = em.createNativeQuery("select * from user_tb where username=? AND password=?", User.class);
        query.setParameter(1,requestDTO.getUsername());
        query.setParameter(2,requestDTO.getPassword());

        try {
            User user = (User) query.getSingleResult();
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    public User findByUsername(String username) {
        Query query = em.createNativeQuery("select * from user_tb where username=?", User.class);
        query.setParameter(1,username);

        try {
            User user = (User) query.getSingleResult();
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    public void update(UserRequest.UpdateDTO requestDTO, int id) {
        Query query = em.createNativeQuery("update user_tb set password = ? where id = ?");
        query.setParameter(1,requestDTO.getPassword());
        query.setParameter(2, id);

        query.executeUpdate();
    }

//    public List<Object> findById(int id) {
//
//    }
}
