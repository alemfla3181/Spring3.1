package springbook.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.*;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class UserDao {
    private Connection c;
    private User user;
    private DataSource dataSource;
    private ConnectionMaker connectionMaker;
    private JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);

        this.dataSource = dataSource;
    }

    public void setConnectionMaker(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }

    public void add(final User user) {
        this.jdbcTemplate.update("insert into users(id, name, password) values (?,?,?)",
            user.getId(), user.getName(), user.getPassword());
    }

    public User get(String id) {
       return this.jdbcTemplate.queryForObject("select * from users where id = ?", new Object[]{id}, new RowMapper<User>() {
           @Override
           public User mapRow(ResultSet rs, int rowNum) throws SQLException {
               User user = new User();
               user.setId(rs.getString("id"));
               user.setName(rs.getString("name"));
               user.setPassword(rs.getString("password"));
               return user;
           }
       });
    }

    public void deleteAll() {
        this.jdbcTemplate.update("delete from users");
    }

    public int getCount() {
        return this.jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
    }

    public List<User> getAll(){
        return this.jdbcTemplate.query("select * from users order by id", new RowMapper<User>(){
            public User mapRow(ResultSet rs, int rowNum) throws SQLException{
                User user = new User();
                user.setId(rs.getString("id"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                return user;
            }
        });
    }

    public class SimpleConnectionMaker {
        public Connection makeNewConnection() throws SQLException, ClassNotFoundException {
            Class.forName("com.mysql.jdbc.Driver");
            java.sql.Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/study", "dragon", "1234");
            return c;
        }
    }

    public void jdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;

        try {
            c = dataSource.getConnection();
            ps = stmt.makePreparedStatement(c);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                }
            }
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e) {
                }
            }
        }
    }
}
