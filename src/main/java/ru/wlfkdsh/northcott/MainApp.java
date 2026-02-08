// src/main/java/ru/wlfkdsh/northcott/MainApp.java
package ru.wlfkdsh.northcott;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.wlfkdsh.northcott.ui.GamePane;

public final class MainApp extends Application {
    @Override
    public void start(Stage stage) {
        GamePane root = new GamePane();
        Scene scene = new Scene(root);

        stage.setTitle("Игра Норкотта (выигрышная стратегия)");
        stage.setScene(scene);
        stage.show();
        stage.sizeToScene();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
