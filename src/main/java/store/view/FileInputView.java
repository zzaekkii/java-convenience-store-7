package store.view;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
}
