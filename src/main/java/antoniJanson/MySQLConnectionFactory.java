package antoniJanson;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnectionFactory{

    public Connection getConnection() throws SQLException {
        return getConnection("localhost", 3306, "coffee_shop", "coffee_shop_user", "password");
    }

    public Connection getConnection(String hostName, int port, String dbname, String userName, String password) throws SQLException {
        String connectionUrl = "jdbc:mysql://%s:%d/%s".formatted(hostName, port, dbname);

        return DriverManager.getConnection(connectionUrl, userName, password);
    }

}