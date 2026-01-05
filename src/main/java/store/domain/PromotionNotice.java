package store.domain;

public enum PromotionNotice {
    ADD_MORE_GIFT("프로모션 추가 가능"),
    OUT_OF_STOCK("프로모션 재고 부족"),
    SATISFIED("조건에 딱 맞음"),
    NOT_PROMOTION("프로모션 아님");

    private final String description;

    PromotionNotice(String description) {
        this.description = description;
    }
}
