package springbook.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.dao.EmptyResultDataAccessException;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.*;

public class UserDao {

    private DataSource dataSource;

    private ConnectionMaker connectionMaker;

    public void setDataSource(DataSource dataSource){
        this.dataSource = dataSource;
    }

    public void setConnectionMaker(ConnectionMaker connectionMaker){
        this.connectionMaker = connectionMaker;
    }
    private Connection c;
    private User user;

    public void add(User user) throws SQLException {
        Connection c = dataSource.getConnection();

        PreparedStatement ps = c.prepareStatement(
                "insert into users(id, name, password) values(?,?,?)");
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        ps.executeUpdate();

        ps.close();
        c.close();
    }

    public User get(String id) throws SQLException {
        Connection c = dataSource.getConnection();

        PreparedStatement ps = c.prepareStatement(
                "select * from users where id =?");
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();

        User user = null;
        if(rs.next()) {
            user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));

        }
        rs.close();
        ps.close();
        c.close();

        if(user == null) throw new EmptyResultDataAccessException(1);

        return user;
    }

    public void deleteAll() throws SQLException{
        Connection c = null;
        PreparedStatement ps = null;

        try {
            c = dataSource.getConnection();

            StatementStrategy strategy = new DeleteAllStatement();
            ps = strategy.makePreparedStatement(c);

            ps.executeUpdate();
        } catch(SQLException e){
            throw e;
        }finally {
            if(ps != null){ try{ ps.close(); } catch (SQLException e){} }
            if(c != null){ try{ c.close(); } catch (SQLException e){} }
        }
    }

    public int getCount() throws SQLException{
        Connection c = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            c = dataSource.getConnection();

            ps = c.prepareStatement("select count(*) from users");

            rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e){
            throw e;
        } finally {
            if(rs != null){ try { rs.close(); } catch (SQLException e){} }
            if(ps!= null) { try{ ps.close(); }catch (SQLException e){} }
            if(c!= null){ try{ c.close(); } catch (SQLException e){} }
        }
    }

    public class SimpleConnectionMaker {
        public Connection makeNewConnection() throws SQLException, ClassNotFoundException{
            Class.forName("com.mysql.jdbc.Driver");
            java.sql.Connection c = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/study", "dragon", "1234");
            return c;
        }
    }

    public void jdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException{
        Connection c = null;
        PreparedStatement ps = null;

        try{
            c = dataSource.getConnection();
            ps = stmt.makePreparedStatement(c);
            ps.executeUpdate();
        } catch (SQLException e){
            throw e;
        } finally {
            if(ps != null){ try {ps.close();} catch (SQLException e){} }
            if(c != null){ try {c.close();} catch (SQLException e){} }
        }
    }
}
