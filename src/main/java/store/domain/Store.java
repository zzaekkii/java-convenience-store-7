package store.domain;

import java.util.ArrayList;
import java.util.List;
import store.exception.ErrorMessage;

public class Store {

    private final List<Product> products;

    public Store(List<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() {
        return new ArrayList<>(products);
    }

    public boolean isExistProduct(String productName) {
        for (Product product : products) {
            if (product.getName().equals(productName)) {
                return true;
            }
        }
        return false;
    }

    public ProductInfo getProductInfo(String productName) {
        for (Product product : products) {
            if (product.getName().equals(productName)) {
                return new ProductInfo(productName, product.getPrice());
            }
        }
        throw new IllegalArgumentException(ErrorMessage.INVALID_PRODUCT.getMessage());
    }

    public boolean canPurchase(String productName, int quantity) {
        int count = 0;
        for (Product product : products) {
            if (product.getName().equals(productName)) {
                count += product.getQuantity();
            }
        }
        return quantity <= count;
    }
}
