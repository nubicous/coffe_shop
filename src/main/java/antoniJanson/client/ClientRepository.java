package antoniJanson.client;

import antoniJanson.CoffeeShop;
import antoniJanson.MySQLConnectionFactory;

import java.sql.*;
import java.util.Optional;

public class ClientRepository {
    private final MySQLConnectionFactory connectionFactory = CoffeeShop.getConnectionFactory();

    public Optional<Client> findByUsername(String username){
        try {
            Connection connection = connectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM clients WHERE username = ?");
            statement.setString(1, username);
            System.out.println(statement.toString());
            statement.executeQuery();
            Client client = null;
            if (statement.getResultSet().next()) {
                client = new Client(
                        statement.getResultSet().getInt("client_id"),
                        statement.getResultSet().getString("phone_number"),
                        statement.getResultSet().getString("name"),
                        statement.getResultSet().getString("surname"),
                        statement.getResultSet().getString("email"),
                        statement.getResultSet().getString("username"),
                        statement.getResultSet().getString("password")
                );
            }
            statement.close();
            connection.close();
            return Optional.ofNullable(client);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to fetch antoniJanson.client");
        }
    }

    public int createClient(String phone, String name, String surname, String email, String username, String password){
        try {
            Connection connection = connectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO clients (phone_number, name, surname, email, username, password)" +
                            " Values(?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, phone);
            statement.setString(2, name);
            statement.setString(3, surname);
            statement.setString(4, email);
            statement.setString(5, username);
            statement.setString(6, password);
            System.out.println(statement.toString());
            statement.executeUpdate();
            int client_id = 0;
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    client_id = keys.getInt(1);
                }else{
                    throw new IllegalStateException("Failed to fetch client_id");
                }
            }
            statement.close();
            connection.close();
            return client_id;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to save antoniJanson.client");
        }
    }
}
