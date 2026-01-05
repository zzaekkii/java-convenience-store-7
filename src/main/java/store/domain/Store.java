package store.domain;

import java.util.ArrayList;
import java.util.List;
import store.domain.receipt.Gift;
import store.domain.receipt.PurchasedProduct;
import store.domain.receipt.Receipt;
import store.exception.ErrorMessage;

public class Store {

    private final List<Product> products;

    public Store(List<Product> products) {
        this.products = products;
    }

    public Receipt purchase(List<OrderItem> orderItems, boolean memberShip, int leftMemberShipLimit) {
        int totalPriceBeforeDiscount = 0;
        int promotionDiscount = 0;
        int membershipDiscount = 0;

        List<PurchasedProduct> purchasedProducts = new ArrayList<>();
        List<Gift> gifts = new ArrayList<>();

        for (OrderItem orderItem : orderItems) {
            purchasedProducts.add(new PurchasedProduct(orderItem.getProduct().name(), orderItem.getQuantity(),
                    orderItem.getProduct().price() * orderItem.getQuantity()));
            if (orderItem.getPromotionCount() == 0) {
                Product product = getProductNotPromotion(orderItem);
                product.soldProduct(orderItem.getQuantity());
                totalPriceBeforeDiscount += product.getPrice() * orderItem.getQuantity();
                continue;
            }

            gifts.add(new Gift(orderItem.getProduct().name(), orderItem.getGiftCount()));
            Product product = getProductPromotion(orderItem);
            int totalProducts = orderItem.getQuantity() + orderItem.getGiftCount();
            totalPriceBeforeDiscount += totalProducts * product.getPrice();
            promotionDiscount += orderItem.getGiftCount() * product.getPrice();

            if (product.getQuantity() >= totalProducts) {
                product.soldProduct(totalProducts);
                continue;
            }

            totalProducts -= product.getQuantity();
            product.soldProduct(product.getQuantity());
            getProductNotPromotion(orderItem).soldProduct(totalProducts);
        }

        if (memberShip) {
            int thirtyPercent = (int) (totalPriceBeforeDiscount * 0.3);
            membershipDiscount = Math.max(leftMemberShipLimit, thirtyPercent);
        }

        int totalPrice = totalPriceBeforeDiscount - promotionDiscount - membershipDiscount;

        return new Receipt(purchasedProducts, gifts, totalPriceBeforeDiscount,
                promotionDiscount, membershipDiscount, totalPrice);
    }

    public boolean isExistProduct(String productName) {
        for (Product product : products) {
            if (product.getName().equals(productName)) {
                return true;
            }
        }
        return false;
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

    public List<Product> getProducts() {
        return new ArrayList<>(products);
    }

    public ProductInfo getProductInfo(String productName) {
        for (Product product : products) {
            if (product.getName().equals(productName)) {
                return new ProductInfo(productName, product.getPrice());
            }
        }
        throw new IllegalArgumentException(ErrorMessage.INVALID_PRODUCT.getMessage());
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

    private Product getProductNotPromotion(OrderItem item) {
        String productName = item.getProduct().name();
        for (Product product : products) {
            if (product.getPromotion().isPresent()) {
                continue;
            }
            if (product.getName().equals(productName)) {
                return product;
            }
        }
        throw new IllegalArgumentException(ErrorMessage.ETC.getMessage());
    }

    private Product getProductPromotion(OrderItem item) {
        String productName = item.getProduct().name();
        for (Product product : products) {
            if (product.getPromotion().isEmpty()) {
                continue;
            }
            if (product.getName().equals(productName)) {
                return product;
            }
        }
        throw new IllegalArgumentException(ErrorMessage.ETC.getMessage());
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
