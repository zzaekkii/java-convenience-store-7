package store;

import store.controller.StoreController;
import store.view.FileInputView;
import store.view.InputView;
import store.view.OutputView;

public class Application {

    public static void main(String[] args) {
        new StoreController(
                new InputView(),
                new OutputView(),
                new FileInputView()
        ).run();
    }
}
