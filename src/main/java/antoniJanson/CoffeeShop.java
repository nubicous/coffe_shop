package antoniJanson;

import antoniJanson.client.ClientService;
import antoniJanson.coffeeData.CoffeeData;
import antoniJanson.coffeeData.CoffeeDataService;
import antoniJanson.order.Order;
import antoniJanson.order.OrderService;
import lombok.Getter;
import org.apache.commons.codec.digest.DigestUtils;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CoffeeShop {
    @Getter
    private final static MySQLConnectionFactory connectionFactory = new MySQLConnectionFactory();
    private final CoffeeDataService coffeeDataService = new CoffeeDataService();
    private final ClientService clientService = new ClientService();

    private final OrderService orderService = new OrderService(clientService);
    private final Scanner sc = new Scanner(System.in);
    private String country = "";
    private String region = "";
    private String producer = "";
    private String type = "";
    private String aroma = "";
    private String acidity = "";
    private String sweetness = "";

    public CoffeeShop() {
    }

    public void showAuthMenu(){
        System.out.println("1.Login");
        System.out.println("2.Register");
        String choice = sc.nextLine();
        if(choice.equals("1")){
            showLoginPrompt();
        }else if(choice.equals("2")){
            showRegisterPrompt();
        }
    }

    private void showRegisterPrompt() {
        System.out.println("Provide your phone number: ");
        String phone = sc.nextLine();
        System.out.println("Provide your name: ");
        String name = sc.nextLine();
        System.out.println("Provide your surname: ");
        String surname = sc.nextLine();
        System.out.println("Provide your email: ");
        String email = sc.nextLine();
        System.out.println("Provide your username: ");
        String username = sc.nextLine();
        while(clientService.checkIfUsernameExists(username)){
            System.out.println("Username is already taken. Please provide new username: ");
            username = sc.nextLine();
        }
        System.out.println("Provide your password consisting over 4 characters: ");
        String password = sc.nextLine();
        while(password.length() < 4){
            System.out.println("Your password is too short! Please provide longer password");
            password = sc.nextLine();
        }
        String pw = DigestUtils.sha256Hex(password);
        clientService.createClient(phone, name, surname, email, username, pw);
        showLoginPrompt();
    }

    private void showLoginPrompt() {
        System.out.println("Provide your username: ");
        String username = sc.nextLine();
        System.out.println("Provide your password: ");
        String password = sc.nextLine();
        if(clientService.loginAuthentication(username, password)){
            showMenu();
        }else{
            System.out.println("Invalid password");
            showAuthMenu();
        }
    }

    public void showMenu(){
        System.out.println("1. Find coffee");
        System.out.println("2. Place order");
        System.out.println("3. View Orders");
        System.out.println("4. Choose delivery method and payment for order");
        String choice = sc.nextLine();
        if(choice.equals("1")){
            showCoffeeBrowseMenu();
        }else if(choice.equals("2")){
            System.out.println("Provide id of coffees you want to order: ");
            List<String> coffee_id_list = new ArrayList<>();
            String coffee_id = sc.nextLine();
            coffee_id_list.add(coffee_id);
            while(!coffee_id.equals("x")){
                System.out.println("Provide id of coffees you want to order. If you want to stop providing next coffees type in x");
                coffee_id = sc.nextLine();
                if(!coffee_id.equals("x"))  coffee_id_list.add(coffee_id);
            }
            placeOrder(coffee_id_list);
            showMenu();
        }else if(choice.equals("3")){
            for(Order order : orderService.viewOrders()){
                System.out.println(order);
            }
            showMenu();
        }else if(choice.equals("4")) {
            System.out.println("Provide the id of order you want to choose delivery and payment method");
            String orderId = sc.nextLine();
            String delivery;
            do {
                System.out.println("Which delivery career do you want to use: UPS, DHL, Inpost");
                delivery = sc.nextLine();
            } while (!isCorrectDelivery(delivery));
            String payment;
            do {
                System.out.println("Which payment method do you want to use: Credit card, Blik, PayPal");
                payment = sc.nextLine();
            } while (!isCorrectPayment(payment));
            orderService.setDeliveryAndPayment(Integer.parseInt(orderId), delivery, payment);
            showMenu();
        }
    }

    private boolean isCorrectDelivery(String delivery){
        return delivery.equalsIgnoreCase("UPS") || delivery.equalsIgnoreCase("DHL") ||
                delivery.equalsIgnoreCase("Inpost");
    }

    private boolean isCorrectPayment(String payment){
        return payment.equalsIgnoreCase("Credit card") || payment.equalsIgnoreCase("Blik") ||
                payment.equalsIgnoreCase("PayPal");
    }

    private void placeOrder(List<String> coffeeIdList) {
        orderService.placeOrder(coffeeIdList);
    }

    public void showCoffeeBrowseMenu(){
        System.out.println("1. Add filter");
        System.out.println("2. Show results");
        System.out.println("3. Clear filters");
        String choice = sc.nextLine();
        if(choice.equals("1")){
            showFilters();
        }else if(choice.equals("2")){
            showResults();
        }else if(choice.equals("3")){
            country = "";
            region = "";
            producer = "";
            type = "";
            aroma = "";
            acidity = "";
            sweetness = "";
            System.out.println("Filters cleared!");
            showCoffeeBrowseMenu();
        }
    }

    private void showFilters() {
        System.out.println("1.Country");
        System.out.println("2.Region");
        System.out.println("3.Producer");
        System.out.println("4.Type");
        System.out.println("5.Aroma");
        System.out.println("6.Acidity");
        System.out.println("7.Sweetness");
        System.out.println("8.Leave");
        String choice = sc.nextLine();
        String value = null;
        if(!choice.equals("8")){
            System.out.println("Input value for filter: ");
            value = sc.nextLine();
        }
        switch (choice){
            case "1" -> country = value;
            case "2" -> region = value;
            case "3" -> producer = value;
            case "4" -> type = value;
            case "5" -> aroma = value;
            case "6" -> acidity = value;
            case "7" -> sweetness = value;
            case "8" -> {
                showCoffeeBrowseMenu();
                return;
            }
            default -> System.out.println("Wrong value, try again");
        }
        showFilters();
    }

    private void showResults() {
        List<CoffeeData> data = coffeeDataService.getCoffeeByParameters(country, region, producer, type, aroma, acidity, sweetness);
        for (CoffeeData coffee : data) {
            System.out.println(coffee);
        }
        showMenu();
    }
}
