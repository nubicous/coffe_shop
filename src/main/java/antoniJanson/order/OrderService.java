package antoniJanson.order;

import antoniJanson.client.ClientService;
import antoniJanson.coffeeData.CoffeeData;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderService {
    private final ClientService clientService;
    private final OrderRepository orderRepository = new OrderRepository();
    private final List<CoffeeData> coffeeDataList = new ArrayList<>();

    public OrderService(final ClientService clientService) {
        this.clientService = clientService;
    }

    public void placeOrder(List<String> coffeeIdList){
        LocalDateTime now = LocalDateTime.now();
        Order order = new Order(clientService.getCurrentClient().getClient_id(), now);
        orderRepository.addOrder(order);
        orderRepository.addCoffeeData(coffeeIdList, order);
    }

    public List<Order> viewOrders(){
        return orderRepository.getClientOrders(clientService.getCurrentClient().getClient_id());
    }

    public void setDeliveryAndPayment(int orderId, String delivery, String payment){
        orderRepository.updateDeliveryAndPayment(orderId, delivery, payment);
    }
}
