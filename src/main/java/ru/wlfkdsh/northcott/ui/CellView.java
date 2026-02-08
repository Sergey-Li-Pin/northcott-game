package ru.wlfkdsh.northcott.ui;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import ru.wlfkdsh.northcott.core.api.Player;

public final class CellView extends StackPane {
    public static final double SIZE = 56.0;

    private final int row;
    private final int col;

    private final Rectangle background;
    private final Circle token;

    private boolean selected;   // выбранная фишка
    private boolean candidate;  // допустимая клетка назначения
    private boolean target;     // выбранная клетка назначения (для клавиатуры)

    public CellView(int row, int col) {
        this.row = row;
        this.col = col;

        this.background = new Rectangle(SIZE, SIZE);
        this.background.setArcWidth(8);
        this.background.setArcHeight(8);
        this.background.setStroke(Color.web("#8a8a8a"));
        this.background.setStrokeWidth(1.0);

        var base = ((row + col) % 2 == 0) ? Color.web("#f3f3f3") : Color.web("#e9e9e9");
        this.background.setFill(base);

        this.token = new Circle(SIZE * 0.28);
        this.token.setVisible(false);

        setMinSize(SIZE, SIZE);
        setMaxSize(SIZE, SIZE);
        setPrefSize(SIZE, SIZE);
        setAlignment(Pos.CENTER);

        getChildren().addAll(background, token);
        refreshBorder();
    }

    public int row() { return row; }
    public int col() { return col; }

    public void setPiece(Player p) {
        if (p == null) {
            token.setVisible(false);
            return;
        }
        token.setVisible(true);

        if (p == Player.WHITE) {
            token.setFill(Color.WHITE);
            token.setStroke(Color.web("#222"));
            token.setStrokeWidth(1.5);
        } else {
            token.setFill(Color.web("#222"));
            token.setStroke(Color.web("#111"));
            token.setStrokeWidth(1.0);
        }
    }

    public void setSelected(boolean value) {
        this.selected = value;
        refreshBorder();
    }

    public void setCandidate(boolean value) {
        this.candidate = value;
        refreshBorder();
    }

    public void setTarget(boolean value) {
        this.target = value;
        refreshBorder();
    }

    public void clearMarks() {
        this.selected = false;
        this.candidate = false;
        this.target = false;
        refreshBorder();
    }

    private void refreshBorder() {
        Color stroke = Color.web("#8a8a8a");
        double width = 1.0;

        if (candidate) {
            stroke = Color.web("#2e7d32"); // зелёный
            width = 3.0;
        }

        if (target) {
            stroke = Color.web("#ef6c00"); // оранжевый
            width = 4.0;
        }

        if (selected) {
            stroke = Color.web("#1565c0"); // синий
            width = 4.0;
        }

        background.setStroke(stroke);
        background.setStrokeWidth(width);
    }
}
