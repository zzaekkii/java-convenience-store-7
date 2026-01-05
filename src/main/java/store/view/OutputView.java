package store.view;

import java.util.List;
import store.domain.Product;
import store.domain.ProductInfo;
import store.domain.receipt.Gift;
import store.domain.receipt.PurchasedProduct;
import store.domain.receipt.Receipt;

public class OutputView {

    public void printErrorMessage(String message) {
        System.out.println(message);
    }

    public void printWelcomeAndProducts(List<Product> products) {
        System.out.println("안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.\n");

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

    public void printMemberShipRequest() {
        System.out.println("멤버십 할인을 받으시겠습니까? (Y/N)");
    }

    public void printReceipt(Receipt receipt) {
        System.out.println("\n==============W 편의점================");
        System.out.printf("%-8s%-7s%-10s\n", "상품명", "수량", "금액");

        int purchasedCount = 0;
        List<PurchasedProduct> purchasedProducts = receipt.purchasedProducts();
        for (PurchasedProduct product : purchasedProducts) {
            purchasedCount += product.quantity();
            System.out.printf("%-8s%-7s%-10s\n", product.name(), product.quantity(), product.price());
        }
        System.out.println("\n============== 증정 ==================");
        List<Gift> gifts = receipt.gifts();
        for (Gift gift : gifts) {
            System.out.printf("%-8s%-7s\n", gift.name(), gift.quantity());
        }
        System.out.println("\n====================================");
        System.out.printf("%-8s%-7s%-10s\n", "총구매액", purchasedCount, receipt.totalAmountBeforeDiscount());
        System.out.printf("%-8s%-7s%-10s\n", "행사할인", "", receipt.promotionDiscount());
        System.out.printf("%-8s%-7s%-10s\n", "멤버십할인", "", receipt.memberShipDiscount());
        System.out.printf("%-8s%-7s%-10s\n\n", "내실돈", "", receipt.totalAmount());
    }

    public void printWhetherPurchaseAgain() {
        System.out.println("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
    }
}
