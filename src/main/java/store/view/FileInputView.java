package store.view;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import store.domain.Product;
import store.domain.Promotion;

public class FileInputView {

    public List<Promotion> readPromotions() throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("src/main/resources/promotions.md"));
        List<Promotion> promotions = new ArrayList<>();

        boolean firstLine = true;
        for (String line : lines) {
            if (firstLine) {
                firstLine = false;
                continue;
            }

            String[] values = line.split(",");

            String name = values[0];
            int buy = Integer.parseInt(values[1]);
            int get = Integer.parseInt(values[2]);
            Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(values[3]);
            Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(values[4]);

            promotions.add(new Promotion(name, buy, get, startDate, endDate));
        }

        return promotions;
    }

    public List<Product> readProducts(List<Promotion> promotions) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("src/main/resources/products.md"));
        List<Product> products = new ArrayList<>();

        Collections.reverse(lines);
        final int len = lines.size() - 1;
        for (int i = 0; i < len; i++) {
            String[] values = lines.get(i).split(",");

            String name = values[0];
            int price = Integer.parseInt(values[1]);
            int quantity = Integer.parseInt(values[2]);
            String promotionValue = values[3];

            boolean notPromotion = true;
            for (Promotion promotion : promotions) {
                if (promotion.name().equals(promotionValue)) {
                    // 프로모션 미적용 상품이 없었으면 재고 없음 등록
                    if (isNotExistNotPromotionProduct(products, name)) {
                        products.add(new Product(name, price, 0, Optional.empty()));
                    }

                    products.add(new Product(name, price, quantity, Optional.of(promotion)));
                    notPromotion = false;
                    break;
                }
            }

            if (notPromotion) {
                products.add(new Product(name, price, quantity, Optional.empty()));
            }
        }

        Collections.reverse(products);

        return products;
    }

    private static boolean isNotExistNotPromotionProduct(List<Product> products, String name) {
        for (Product product : products) {
            if (product.getName().equals(name)) {
                return false;
            }
        }
        return true;
    }
}
