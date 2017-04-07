package se.build;

/**
 * Name: admin
 * Date: 2017/4/7
 * Time: 18:55
 */
public class Test {
    public static void main(String[] args) {
        NutritionFacts cocaCola = new NutritionFacts.Builder(248,8).calories(100)
                .sodium(35).carbohydrate(27).build();
        System.out.println(cocaCola.getCalories());
    }
}
