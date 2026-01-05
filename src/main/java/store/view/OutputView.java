package store.view;

import java.util.List;
import store.domain.Product;

public class OutputView {

    public void printErrorMessage(String message) {
        System.out.println(message);
    }

    public void printWelcomeAndProducts(List<Product> products) {
        System.out.println("안녕하세요. W편의점입니다.\n"
                + "현재 보유하고 있는 상품입니다.\n");

        for (Product product : products) {
            System.out.println("- " + product.getName() + " " + product.getPriceAsString() + " "
                    + product.getQuantityAsString() + " " + product.getPromotionAsString());
        }

        System.out.println("\n구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
    }
}
