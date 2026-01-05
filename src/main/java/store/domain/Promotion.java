package store.domain;

import camp.nextstep.edu.missionutils.DateTimes;
import java.sql.Timestamp;
import java.util.Date;

public record Promotion(
        String name,
        int required,
        int gifts,
        Date start_date,
        Date end_date
) {
    public boolean isIncludeToday() {
        Date today = Timestamp.valueOf(DateTimes.now());
        if (today.before(start_date) || today.after(end_date)) {
            return false;
        }
        return true;
    }
}
