package cn.ac.iscas.dmo.connector.jdbc;

import cn.ac.iscas.dmo.connector.util.Base64Utils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.sql.*;

/**
 * @author zhuquanwen
 * @version 1.0
 * @date 2024/11/06
 */
@RunWith(JUnit4.class)
public class TdEngineConnectionImplTest {
    Connection connection = null;

    @Before
    public void testConnection() {
        String datasourceType = Base64Utils.encodeToStr("tdengine3");
        String datasourceName = Base64Utils.encodeToStr("test-td-95");
        try {
            Class.forName("cn.ac.iscas.dmo.connector.jdbc.Driver");
//            connection = DriverManager.getConnection("jdbc:dmo://192.168.50.49:3282?sqlServiceUrl=" + sqlServiceUrl);
            connection = DriverManager.getConnection("jdbc:dmo://192.168.50.49:4282?datasourceType="
                    + datasourceType + "&datasourceName=" + datasourceName);
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
        ResultSet rs = statement.executeQuery("select * from ods_test");
        while (rs.next()) {
            System.out.println(rs.getObject("c1"));
        }
    }

    /**
     * limit
     */
    @Test
    public void testStatement2() throws SQLException {
        Statement statement = connection.createStatement();
        System.out.println(statement);
        ResultSet rs = statement.executeQuery("select * from ods_test limit 0, 1");
        while (rs.next()) {
            System.out.println(rs.getString("c2"));
        }
    }

    /**
     * 别名
     */
    @Test
    public void testStatement3() throws SQLException {
        Statement statement = connection.createStatement();
        System.out.println(statement);
        ResultSet rs = statement.executeQuery("select c2 AS lala from ods_test t limit 0, 1");
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
        ResultSet rs = statement.executeQuery("select count(*) from ods_test t");
        while (rs.next()) {
            System.out.println(rs.getObject("count(*)"));
        }
    }


    /**
     * 测试preparedstatement
     */
    @Test
    public void testPreparedStatement() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("select * from ods_test where c2 = ?");
        preparedStatement.setDouble(1, 1.8);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            System.out.println(resultSet.getObject("c1"));
        }
    }

}