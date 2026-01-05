package store.view;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import store.domain.Product;
import store.domain.Promotion;

public class FileInputView {

    public List<Promotion> readPromotions() throws IOException, ParseException {
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

    public List<Product> readProducts(List<Promotion> promotions) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("src/main/resources/products.md"));
        List<Product> products = new ArrayList<>();

        boolean firstLine = true;
        for (String line : lines) {
            if (firstLine) {
                firstLine = false;
                continue;
            }

            String[] values = line.split(",");

            String name = values[0];
            int price = Integer.parseInt(values[1]);
            int quantity = Integer.parseInt(values[2]);
            String promotionValue = values[3];

            boolean existPromotion = false;
            for (Promotion promotion : promotions) {
                if (promotion.name().equals(promotionValue)) {
                    products.add(new Product(name, price, quantity, Optional.of(promotion)));
                    existPromotion = true;
                    break;
                }
            }

            if (!existPromotion) {
                products.add(new Product(name, price, quantity, Optional.empty()));
            }
        }

        return products;
    }
}
