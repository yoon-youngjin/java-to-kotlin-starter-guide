package com.lannstark.lec17;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Lec17Main {

    public static void main(String[] args) {
        List<Fruit> fruits = Arrays.asList(
                new Fruit("사과", 1_000),
                new Fruit("사과", 1_200),
                new Fruit("사과", 1_200),
                new Fruit("사과", 1_500),
                new Fruit("바나나", 3_000),
                new Fruit("바나나", 3_200),
                new Fruit("바나나", 2_500),
                new Fruit("수박", 10_000)
        );

        filterFruits(fruits, fruit -> fruit.getName().equals("사과"));
//        filterFruits(fruits, new FruitFilter() {
//            @Override
//            public boolean isSelected(Fruit fruit) {
//                return Arrays.asList("사과", "바나나").contains(fruit.getName()) &&
//                        fruit.getPrice() > 5000;
//            }
//        });

    }

    private List<Fruit> findApples(List<Fruit> fruits) {
        List<Fruit> apples = new ArrayList<>();
        for (Fruit fruit : fruits) {
            if (fruit.getName().equals("사과")) {
                apples.add(fruit);
            }
        }
        return apples;
    }

    private List<Fruit> findFruitsWithName(List<Fruit> fruits, String name) {
        List<Fruit> results = new ArrayList<>();
        for (Fruit fruit : fruits) {
            if (fruit.getName().equals(name)) {
                results.add(fruit);
            }
        }
        return results;
    }

//    private static List<Fruit> filterFruits(List<Fruit> fruits, Predicate<Fruit> fruitFilter) {
//        List<Fruit> results = new ArrayList<>();
//        for (Fruit fruit : fruits) {
//            if (fruitFilter.test(fruit)) {
//                results.add(fruit);
//            }
//        }
//        return results;
//    }

//    private static List<Fruit> filterFruits(List<Fruit> fruits, FruitFilter fruitFilter) {
//        List<Fruit> results = new ArrayList<>();
//        for (Fruit fruit : fruits) {
//            if (fruitFilter.isSelected(fruit)) {
//                results.add(fruit);
//            }
//        }
//        return results;
//    }

    private static List<Fruit> filterFruits(List<Fruit> fruits, Predicate<Fruit> fruitFilter) {
        return fruits.stream()
                .filter(fruitFilter)
                .collect(Collectors.toList());
    }
}
