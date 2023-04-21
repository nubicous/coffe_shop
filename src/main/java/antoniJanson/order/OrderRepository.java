package antoniJanson.order;

import antoniJanson.CoffeeShop;
import antoniJanson.MySQLConnectionFactory;
import antoniJanson.coffeeData.CoffeeData;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {
    private final MySQLConnectionFactory connectionFactory = CoffeeShop.getConnectionFactory();

    public void addOrder(Order order){
        try {
            Connection connection = connectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO orders (fkey_client_id, datetime_of_order) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, order.getFkey_client_id());
            statement.setString(2, order.getDatetime_of_order().toString());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    order.setOrder_id(keys.getInt(1));
                }else{
                    throw new IllegalStateException("Failed to fetch order_id");
                }
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to place new order!");
        }
    }

    public void addCoffeeData(List<String> coffeeIdList, Order order) {
        try {
            Connection connection = connectionFactory.getConnection();
            for(String elem : coffeeIdList){
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO coffee_in_orders (order_id, coffee_ordered) VALUES (?, ?)");
                statement.setInt(1, order.getOrder_id());
                statement.setString(2, elem);
                statement.executeUpdate();
                statement.close();
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to place new order!");
        }
    }

    public List<Order> getClientOrders(final int client_id) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            Connection connection = connectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM orders WHERE fkey_client_id = ?");
            statement.setInt(1, client_id);
            statement.executeQuery();
            int orderId;
            List<Order> orders = new ArrayList<>();
            while (statement.getResultSet().next()) {
                String dateTimeOfOrder = statement.getResultSet().getString("datetime_of_order");
                String delivery = statement.getResultSet().getString("delivery");
                String payment = statement.getResultSet().getString("payment");
                LocalDateTime dateTime = LocalDateTime.parse(dateTimeOfOrder, formatter);
                orderId = statement.getResultSet().getInt("order_id");
                Order order = new Order(client_id, dateTime);
                order.setOrder_id(orderId);
                order.setListOfCoffeeData(loadOrderCoffeeData(orderId));
                if(!payment.equals("") || !delivery.equals("")){
                    order.setPayment(payment);
                    order.setDelivery(delivery);
                }
                orders.add(order);
            }
            statement.close();
            connection.close();
            return orders;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to fetch coffee");
        }
    }

    private List<CoffeeData> loadOrderCoffeeData(int orderId){
        try {
            List<CoffeeData> coffeeDataList = new ArrayList<>();
            Connection connection = connectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT cd.coffee_id, cd.country, cd.region, cd.producer, cd.type, cd.amount, cd.mass," +
                            " cd.aroma, cd.acidity, cd.sweetness, cd.grade, cd.price_in_usd FROM coffee_in_orders" +
                            " JOIN coffee_data cd on coffee_in_orders.coffee_ordered = cd.coffee_id " +
                            "where order_id = ?;");
            statement.setInt(1, orderId);
            statement.executeQuery();
            while (statement.getResultSet().next()) {
                coffeeDataList.add(new CoffeeData(
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
            return coffeeDataList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to fetch coffee");
        }
    }

    public void updateDeliveryAndPayment(int orderId, String delivery, String payment){
        try {
            Connection connection = connectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "Update orders set payment = ?, delivery = ? where order_id = ?");
            statement.setString(1, delivery);
            statement.setString(2, payment);
            statement.setInt(3, orderId);
            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to fetch coffee");
        }
    }
}
