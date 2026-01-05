package store.domain;

public class OrderItem {
    private final ProductInfo product;
    private int quantity = 0;

    public OrderItem(ProductInfo product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public void addProduct(int quantity) {
        this.quantity += quantity;
    }

    public void takeOutProduct(int quantity) {
        this.quantity -= quantity;
    }

    public ProductInfo getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}
