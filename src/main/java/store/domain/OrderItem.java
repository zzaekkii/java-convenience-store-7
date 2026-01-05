package store.domain;

public record OrderItem(
        ProductInfo product,
        int quantity
) {
}
