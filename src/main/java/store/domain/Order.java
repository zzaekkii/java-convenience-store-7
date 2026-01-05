package store.domain;

import java.util.List;

public record Order(
        List<Product> product,
        int quantity
) {
}
