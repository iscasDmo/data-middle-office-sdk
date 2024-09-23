package cn.ac.iscas.dmo.connector.jdbc;

import cn.ac.iscas.dmo.connector.Constants;
import cn.ac.iscas.dmo.connector.conf.ConnectionUrl;
import cn.ac.iscas.dmo.connector.conf.HostInfo;
import cn.ac.iscas.dmo.connector.util.StringUtils;

import java.sql.Connection;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * 中台连接的driver
 *
 * @author zhuquanwen
 * @version 1.0
 * @date 2024/9/23 13:27
 */

public class Driver implements java.sql.Driver {

    static {
        try {
            java.sql.DriverManager.registerDriver(new Driver());
        } catch (SQLException e) {
            throw new RuntimeException("Can't register driver!");
        }
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        if (!ConnectionUrl.acceptsUrl(url)) {
            return null;
        }
        return ConnectionImpl.getInstance(HostInfo.getInstance(url, info));
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return ConnectionUrl.acceptsUrl(url);
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return new DriverPropertyInfo[0];
    }

    @Override
    public int getMajorVersion() {
        return StringUtils.safeIntParse(Constants.MAJOR_VERSION);
    }

    @Override
    public int getMinorVersion() {
        return StringUtils.safeIntParse(Constants.MINOR_VERSION);
    }

    @Override
    public boolean jdbcCompliant() {
        return false;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }
}
