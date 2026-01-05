package store.domain;

import java.util.Date;

public record Promotion(
        String name,
        String buy,
        String get,
        Date start_date,
        Date end_date
) {
}
