package antoniJanson.coffeeData;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CoffeeData {
    private int coffee_id;
    private String country;
    private String region;
    private String producer;
    private String type;
    private int amount;
    private double mass;
    private double aroma;
    private double acidity;
    private double sweetness;
    private double grade;
    private double price_in_usd;


    @Override
    public String toString() {
        return "Coffee{" +
                "coffee_id=" + coffee_id +
                ", country='" + country + '\'' +
                ", region='" + region + '\'' +
                ", producer='" + producer + '\'' +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                ", mass=" + mass +
                ", aroma=" + aroma +
                ", acidity=" + acidity +
                ", sweetness=" + sweetness +
                ", grade=" + grade +
                ", price_in_usd=" + price_in_usd +
                '}';
    }
}
