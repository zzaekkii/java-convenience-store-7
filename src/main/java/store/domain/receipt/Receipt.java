package store.domain.receipt;

import java.util.List;

public record Receipt(
        List<PurchasedProduct> purchasedProducts,
        List<Gift> gifts,
        int totalAmountBeforeDiscount,
        int promotionDiscount,
        int memberShipDiscount,
        int totalAmount
) {
}
