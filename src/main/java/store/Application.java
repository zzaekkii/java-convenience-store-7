package store;

import java.io.IOException;
import java.text.ParseException;
import store.controller.StoreController;
import store.view.FileInputView;
import store.view.InputView;
import store.view.OutputView;

public class Application {

    public static void main(String[] args) throws IOException, ParseException {

        new StoreController(
                new InputView(),
                new OutputView(),
                new FileInputView()
        ).run();
    }
}
