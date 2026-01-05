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

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public Optional<Promotion> getPromotion() {
        return promotion;
    }

    public String getPriceAsString() {
        return String.format("%,d원", price);
    }

    public String getQuantityAsString() {
        if (quantity == 0) {
            return "재고 없음";
        }
        return String.format("%,d개", quantity);
    }

    public String getPromotionAsString() {
        if (promotion.isEmpty()) {
            return "";
        }
        return promotion.get().name();
    }
}
