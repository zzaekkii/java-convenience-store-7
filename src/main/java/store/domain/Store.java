package store.domain;

import java.util.List;

public class Store {

    private final List<Product> products;

    public Store(List<Product> products) {
        this.products = products;
    }
}
