package store.domain;

import java.util.List;

public record Receipt(
        List<Product> purchasedProducts,
        List<Product> giveaways,
        int totalAmountBeforeDiscount,
        // 행사할인(프로모션 할인)은 giveaways의 가격 총합
        int memberShipDiscount,
        int totalAmount
) {
}
