package antoniJanson.order;

import antoniJanson.coffeeData.CoffeeData;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Order {
    private int order_id;
    private int fkey_client_id;
    private LocalDateTime datetime_of_order;
    private String payment = "";
    private String delivery = "";
    private List<CoffeeData> listOfCoffeeData = new ArrayList<>();

    Order(final int fkey_client_id, final LocalDateTime datetime_of_order) {
        this.fkey_client_id = fkey_client_id;
        this.datetime_of_order = datetime_of_order;
    }

    @Override
    public String toString() {
        return "Order{" +
                "order_id=" + order_id +
                ", fkey_client_id=" + fkey_client_id +
                ", datetime_of_order=" + datetime_of_order +
                ", payment='" + payment + '\'' +
                ", delivery='" + delivery + '\'' +
                ", listOfCoffeeData=" + listOfCoffeeData.toString() +
                '}';
    }
}
