package store.controller;

import java.util.List;
import store.domain.OrderItem;
import store.domain.Product;
import store.domain.Promotion;
import store.domain.Store;
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

        while (continuePurchase) {
            List<OrderItem> orderItems = welcome(store);
            applyPromotion(store, orderItems);
            break;
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
