package store.view;

import java.util.List;
import store.domain.Product;
import store.domain.ProductInfo;

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

    public void printAddProductRequest(ProductInfo product, int gifts) {
        System.out.println("현재 " + product.name() + "은(는) " + gifts + "개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)");
    }

    public void printPurchaseProductWithoutPromotionRequest(ProductInfo product, int quantity) {
        System.out.println("현재 " + product.name() + " " + quantity + "개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)");
    }
}
