package antoniJanson.coffeeData;

import java.util.List;

public class CoffeeDataService {
    private final CoffeeDataRepository coffeeDataRepository = new CoffeeDataRepository();
    public List<CoffeeData> getCoffeeByParameters(String country, String region, String producer, String type,
                                                  String aroma, String acidity, String sweetness){
        return coffeeDataRepository.getCoffeeByParameters(country, region, producer, type, aroma,
                acidity, sweetness);
    }
}
