package springbook.user.main;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import springbook.user.dao.DaoFactory;
import springbook.user.dao.UserDao;
import springbook.user.domain.User;

import java.sql.SQLException;

public class UserDaoTest {
    public static void main(String[] args) {
        ApplicationContext
                context = new GenericXmlApplicationContext("applicationContext.xml");

        UserDao dao = context.getBean("userDao", UserDao.class);

        User user = new User();
        user.setId("user");
        user.setName("용가리");
        user.setPassword("1234");
        user.setEmail("dragon@naver.com");

        dao.add(user);

        System.out.println(user.getId() + " 등록 성공");

        User user2 = dao.get(user.getId());

        if(!user.getName().equals(user2.getName())) {
            System.out.println("테스트 실패 (name)");
        }else if(!user.getPassword().equals(user2.getPassword())) {
            System.out.println("테스트 실패 (password)");
        }else if(!user.getEmail().equals(user2.getEmail())){
            System.out.println("테스트 실패 (email)");
        }else {
            System.out.println("조회 테스트 성공");
        }
    }

    public void addAndGet() throws SQLException{
        ApplicationContext context = new
                ClassPathXmlApplicationContext("ApplicationContext.xml");

        UserDao dao = context.getBean("userDao", UserDao.class);

    }

}
