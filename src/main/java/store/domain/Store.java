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

    /**
     * 만들고 보니 크게 필요가 없네 세밀한 작업을 컨트롤러에서 하게 되는데 책임 분리가 덜 된 듯 실제 쓰임새있는건 NOT_PROMOTION만
     */
    public PromotionNotice canPromotion(OrderItem item) {
        String productName = item.getProduct().name();
        int quantity = item.getQuantity();

        if (isNotExistPromotion(productName)) {
            return PromotionNotice.NOT_PROMOTION;
        }

        Promotion promotion = getPromotion(productName);
        if (!promotion.isIncludeToday()) {
            return PromotionNotice.NOT_PROMOTION;
        }

        int stocks = getPromotionProductCount(productName);
        if (quantity > stocks) {
            return PromotionNotice.OUT_OF_STOCK;
        }

        int required = promotion.required();
        int giftsPerPromotion = promotion.gifts();
        int promotionSet = required + giftsPerPromotion;
        if (quantity % promotionSet == 0) {
            return PromotionNotice.SATISFIED;
        }

        return PromotionNotice.ADD_MORE_GIFT;
    }

    public int getPromotionProductStocks(OrderItem item) {
        String productName = item.getProduct().name();
        if (isNotExistPromotion(productName)) {
            throw new IllegalArgumentException(ErrorMessage.ETC.getMessage());
        }
        return getPromotionProductCount(productName);

    }

    public Promotion getPromotionForProduct(OrderItem item) {
        String productName = item.getProduct().name();
        if (isNotExistPromotion(productName)) {
            throw new IllegalArgumentException(ErrorMessage.ETC.getMessage());
        }
        return getPromotion(productName);
    }

    private boolean isNotExistPromotion(String productName) {
        for (Product product : products) {
            if (product.getPromotion().isEmpty()) {
                continue;
            }
            if (product.getName().equals(productName)) {
                return true;
            }
        }
        return false;
    }

    private Promotion getPromotion(String productName) {
        for (Product product : products) {
            if (product.getPromotion().isEmpty()) {
                continue;
            }
            if (product.getName().equals(productName)) {
                return product.getPromotion().get();
            }
        }
        throw new IllegalArgumentException(ErrorMessage.ETC.getMessage());
    }

    private int getPromotionProductCount(String productName) {
        for (Product product : products) {
            if (product.getPromotion().isEmpty()) {
                continue;
            }
            if (product.getName().equals(productName)) {
                return product.getQuantity();
            }
        }
        return 0;
    }
}
