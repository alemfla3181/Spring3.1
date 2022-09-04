package springbook.user.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springbook.user.dao.ConnectionMaker;
import springbook.user.dao.DConnectionMaker;
import springbook.user.dao.UserDao;
import springbook.user.domain.User;

import java.sql.SQLException;

//@SpringBootApplication
public class UserApplication {

//    public static void main(String[] args) {
//        SpringApplication.run(UserApplication.class, args);
//    }

//    public static void main(String[] args) throws SQLException, ClassNotFoundException {
//        UserDao dao = new UserDao();
//
//        User user = new User();
//        user.setId("dragon");
//        user.setName("김덕배");
//        user.setPassword("1234");
//
//        dao.add(user);
//
//        System.out.println(user.getId() + "등록 성공");
//
//        User user2 = dao.get(user.getId());
//        System.out.println(user2.getName());
//
//        System.out.println(user2.getPassword());
//
//        System.out.println(user2.getId() + "조회 성공");
//
//    }

}