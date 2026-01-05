package store.domain.receipt;

public record PurchasedProduct(
        String name,
        int quantity,
        int price
) {
}
