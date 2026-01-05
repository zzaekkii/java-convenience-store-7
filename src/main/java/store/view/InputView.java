package store.view;

import static store.exception.ErrorMessage.ETC;
import static store.exception.ErrorMessage.INVALID_FORMAT;
import static store.exception.ErrorMessage.INVALID_PRODUCT;
import static store.exception.ErrorMessage.OUT_OF_STOCK;

import camp.nextstep.edu.missionutils.Console;
import java.util.ArrayList;
import java.util.List;
import store.domain.OrderItem;
import store.domain.Store;

public class InputView {

    public List<OrderItem> readOrders(Store store) {
        String input = readAndValidate();
        validateSeparator(input);

        List<OrderItem> orderItems = new ArrayList<>();

        String[] values = input.split(",");
        for (String value : values) {
            validateSeparator(value);

            String[] tokens = value.substring(1, value.length() - 1).split("-");

            String productName = tokens[0];

            if (!productName.matches("^[가-힣]+$")) {
                throw new IllegalArgumentException(ETC.getMessage());
            }

            if (!store.isExistProduct(productName)) {
                throw new IllegalArgumentException(INVALID_PRODUCT.getMessage());
            }

            int productCount = getProductCount(tokens[1]);

            if (!store.canPurchase(productName, productCount)) {
                throw new IllegalArgumentException(OUT_OF_STOCK.getMessage());
            }

            orderItems.add(new OrderItem(store.getProductInfo(productName), productCount));
        }

        return orderItems;
    }

    private static int getProductCount(String token) {
        try {
            return Integer.parseInt(token);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ETC.getMessage());
        }
    }

    private static String readAndValidate() {
        String input = readLine();

        nullCheck(input);

        input = input.trim();
        return input;
    }

    private static void validateSeparator(String value) {
        if (!value.startsWith("[") || !value.endsWith("]")) {
            throw new IllegalArgumentException(INVALID_FORMAT.getMessage());
        }

        if (value.contains(",,")) {
            throw new IllegalArgumentException(INVALID_FORMAT.getMessage());
        }

        if (value.contains("--")) {
            throw new IllegalArgumentException(INVALID_FORMAT.getMessage());
        }

        if (value.contains(" ")) {
            throw new IllegalArgumentException(INVALID_FORMAT.getMessage());
        }
    }

    private static String readLine() {
        return Console.readLine();
    }

    private static void nullCheck(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException(ETC.getMessage());
        }
    }
}