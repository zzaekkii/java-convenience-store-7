package store.domain;

import java.util.Optional;

public class OrderItem {
    private final ProductInfo product;
    private int quantity = 0;
    private Optional<Promotion> promotion = Optional.empty();
    private int giftCount = 0;
    private int promotionCount = 0;

    public OrderItem(ProductInfo product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    /**
     * 프로모션 적용된만큼, 원래 사려던 양 중 일부가 증정으로 이동
     */
    public void appliedPromotion(Promotion promotion, int promotionCount) {
        if (promotionCount == 0) {
            return;
        }
        this.promotion = Optional.ofNullable(promotion);
        int gifts = promotion.gifts() * promotionCount;
        this.quantity -= gifts;
        this.giftCount = gifts;
        this.promotionCount = promotionCount;
    }

    public void addGift(int giftCount) {
        this.giftCount += giftCount;
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

    public int getGiftCount() {
        return giftCount;
    }

    public int getPromotionCount() {
        return promotionCount;
    }
}
