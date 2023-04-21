package antoniJanson.coffeeData;

import antoniJanson.CoffeeShop;
import antoniJanson.MySQLConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CoffeeDataRepository {

    private final MySQLConnectionFactory connectionFactory = CoffeeShop.getConnectionFactory();
    public List<CoffeeData> getCoffeeByParameters(String country, String region, String producer, String type,
                                                  String aroma, String acidity, String sweetness){
        List<CoffeeData> coffeeItems = new ArrayList<>();
        try {
            Connection connection = connectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM coffee_data WHERE country LIKE ? and  region LIKE ? and producer LIKE ? and type LIKE ? " +
                            "and aroma LIKE ? and acidity LIKE ? and sweetness LIKE ?");
            statement.setString(1, "%" + country + "%");
            statement.setString(2, "%" + region + "%");
            statement.setString(3, "%" + producer + "%");
            statement.setString(4, "%" + type + "%");
            statement.setString(5, "%" + aroma + "%");
            statement.setString(6, "%" + acidity + "%");
            statement.setString(7, "%" + sweetness + "%");
            statement.executeQuery();
            while (statement.getResultSet().next()) {
                coffeeItems.add(new CoffeeData(
                        statement.getResultSet().getInt("coffee_id"),
                        statement.getResultSet().getString("country"),
                        statement.getResultSet().getString("region"),
                        statement.getResultSet().getString("producer"),
                        statement.getResultSet().getString("type"),
                        statement.getResultSet().getInt("amount"),
                        statement.getResultSet().getDouble("mass"),
                        statement.getResultSet().getDouble("aroma"),
                        statement.getResultSet().getDouble("acidity"),
                        statement.getResultSet().getDouble("sweetness"),
                        statement.getResultSet().getDouble("grade"),
                        statement.getResultSet().getDouble("price_in_usd")

                ));
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to fetch coffee");
        }
        return coffeeItems;
    }
}
