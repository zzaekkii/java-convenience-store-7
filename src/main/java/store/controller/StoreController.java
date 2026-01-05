package store.controller;

import java.util.List;
import store.domain.OrderItem;
import store.domain.Product;
import store.domain.Promotion;
import store.domain.PromotionNotice;
import store.domain.Store;
import store.domain.receipt.Receipt;
import store.exception.ErrorMessage;
import store.view.FileInputView;
import store.view.InputView;
import store.view.OutputView;

public class StoreController {

    private final InputView inputView;
    private final OutputView outputView;
    private final FileInputView fileInputView;

    private final boolean continuePurchase = true;

    public StoreController(InputView inputView, OutputView outputView, FileInputView fileInputView) {
        this.inputView = inputView;
        this.outputView = outputView;
        this.fileInputView = fileInputView;
    }

    public void run() {
        Store store = new Store(readProductsFromMd(readPromotionsFromMd()));
        int leftMemberShipLimit = 8000;

        while (continuePurchase) {
            List<OrderItem> orderItems = welcome(store);
            applyPromotion(store, orderItems);
            Receipt receipt = store.purchase(orderItems, applyMemberShip(), leftMemberShipLimit);
        }
    }

    private List<OrderItem> welcome(Store store) {
        while (true) {
            outputView.printWelcomeAndProducts(store.getProducts());

            try {
                return inputView.readOrders(store);
            } catch (IllegalArgumentException e) {
                outputView.printErrorMessage(e.getMessage());
            }
        }
    }

    private void applyPromotion(Store store, List<OrderItem> orderItems) {
        for (OrderItem item : orderItems) {
            PromotionNotice promotionNotice = store.canPromotion(item);

            if (promotionNotice.equals(PromotionNotice.NOT_PROMOTION)) {
                continue;
            }

            Promotion promotion = store.getPromotionForProduct(item);
            int required = promotion.required();
            int giftsPerPromotion = promotion.gifts();
            int promotionSet = required + giftsPerPromotion;

            int orderQuantity = item.getQuantity();
            if (promotionNotice.equals(PromotionNotice.SATISFIED)) {
                item.appliedPromotion(promotion, orderQuantity / promotionSet);
                continue;
            }

            if (promotionNotice.equals(PromotionNotice.OUT_OF_STOCK)) {
                int stocks = store.getPromotionProductStocks(item);
                item.appliedPromotion(promotion, stocks / promotionSet);

                noticeWithoutPromotion(item, orderQuantity - stocks / promotionSet * promotionSet);
                continue;
            }

            item.appliedPromotion(promotion, orderQuantity / promotionSet);
            if (orderQuantity % promotionSet >= required) {
                requestAddPromotionProduct(item, giftsPerPromotion);
                continue;
            }
            noticeWithoutPromotion(item, orderQuantity % promotionSet);
        }
    }

    private void noticeWithoutPromotion(OrderItem item, int quantity) {
        while (true) {
            outputView.printPurchaseProductWithoutPromotionRequest(item.getProduct(), quantity);
            try {
                if (inputView.readCommand()) {
                    return;
                }
                item.takeOutProduct(quantity);
                return;
            } catch (IllegalArgumentException e) {
                outputView.printErrorMessage(e.getMessage());
            }
        }
    }

    private void requestAddPromotionProduct(OrderItem item, int gifts) {
        while (true) {
            outputView.printAddProductRequest(item.getProduct(), gifts);
            try {
                if (inputView.readCommand()) {
                    item.addGift(gifts);
                    return;
                }
                return;
            } catch (IllegalArgumentException e) {
                outputView.printErrorMessage(e.getMessage());
            }
        }
    }

    private boolean applyMemberShip() {
        while (true) {
            outputView.printMemberShipRequest();
            try {
                return inputView.readCommand();
            } catch (IllegalArgumentException e) {
                outputView.printErrorMessage(e.getMessage());
            }
        }
    }

    private List<Promotion> readPromotionsFromMd() {
        try {
            return fileInputView.readPromotions();
        } catch (Exception e) {
            outputView.printErrorMessage(ErrorMessage.ETC.getMessage());
        }
        return null;
    }

    private List<Product> readProductsFromMd(List<Promotion> promotions) {
        try {
            return fileInputView.readProducts(promotions);
        } catch (Exception e) {
            outputView.printErrorMessage(ErrorMessage.ETC.getMessage());
        }
        return null;
    }
}
