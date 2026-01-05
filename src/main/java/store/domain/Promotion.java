package store.domain;

import java.util.Date;

public record Promotion(
        String name,
        int buy,
        int get,
        Date start_date,
        Date end_date
) {
}
