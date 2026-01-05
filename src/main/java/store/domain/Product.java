package store.domain;

import java.util.Optional;

public class Product {
    private final String name;
    private final int price;
    private final int quantity;
    private final Optional<Promotion> promotion;


    public Product(String name, int price, int quantity, Optional<Promotion> promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }
}
