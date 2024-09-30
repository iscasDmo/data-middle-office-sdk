package cn.ac.iscas.dmo.connector.jdbc;

import cn.ac.iscas.dmo.connector.util.Base64Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

/**
 * @author zhuquanwen
 * @version 1.0
 * @date 2024/9/23 16:20
 */

class ConnectionImplTest {
    Connection connection = null;

    @BeforeEach
    public void testConnection() {
        String sqlServiceUrl = "/dmo/data-service/主题域1/mysql-dmo/DYNAMIC_SQL/1727079751976";
        sqlServiceUrl = Base64Utils.encodeToStr(sqlServiceUrl);
        try {
            Class.forName("cn.ac.iscas.dmo.connector.jdbc.Driver");
//            connection = DriverManager.getConnection("jdbc:dmo://192.168.50.49:3282?sqlServiceUrl=" + sqlServiceUrl);
            connection = DriverManager.getConnection("jdbc:dmo://192.168.50.49:4282?datasourceType=mysql5&datasourceName=mysql-dmo");
            System.out.println(connection);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 全查
     */
    @Test
    public void test() throws SQLException {
        Statement statement = connection.createStatement();
        System.out.println(statement);
        ResultSet rs = statement.executeQuery("select * from dict_data");
        while (rs.next()) {
            System.out.println(rs.getString("dict_type"));
        }
    }

    /**
     * limit
     */
    @Test
    public void testStatement2() throws SQLException {
        Statement statement = connection.createStatement();
        System.out.println(statement);
        ResultSet rs = statement.executeQuery("select * from dict_data limit 0, 1");
        while (rs.next()) {
            System.out.println(rs.getString("dict_type"));
        }
    }

    /**
     * 别名
     */
    @Test
    public void testStatement3() throws SQLException {
        Statement statement = connection.createStatement();
        System.out.println(statement);
        ResultSet rs = statement.executeQuery("select dict_type AS lala from dict_data t limit 0, 1");
        while (rs.next()) {
            System.out.println(rs.getObject("lala"));

        }
    }

    /**
     * count(*)
     */
    @Test
    public void testCount() throws SQLException {
        Statement statement = connection.createStatement();
        System.out.println(statement);
        ResultSet rs = statement.executeQuery("select count(*) from dict_data t");
        while (rs.next()) {
            System.out.println(rs.getObject("count(*)"));
        }
    }

    /**
     * 测试修改
     */
    @Test
    public void testUpdate() throws SQLException {
        Statement statement = connection.createStatement();
        System.out.println(statement);
        int count = statement.executeUpdate("update dict_data set update_by = 'zs' where id = 1");
        System.out.println(count);
    }

    /**
     * 测试preparedstatement
     */
    @Test
    public void testPreparedStatement() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("select * from dict_data where dict_type = ?");
        preparedStatement.setString(1, "系统类");
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            System.out.println(resultSet.getString("dict_type"));
        }
    }

}