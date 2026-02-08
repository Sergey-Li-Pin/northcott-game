package ru.wlfkdsh.northcott.ui;

import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import ru.wlfkdsh.northcott.core.api.BoardSnapshot;
import ru.wlfkdsh.northcott.core.api.Player;

import java.util.function.BiConsumer;

public final class BoardView extends GridPane {
    private final int rows;
    private final int cols;
    private final CellView[][] cells;

    private BiConsumer<Integer, Integer> onCellClicked;

    public BoardView(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.cells = new CellView[rows][cols];

        setPadding(new Insets(12));
        setHgap(6);
        setVgap(6);

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                CellView cell = new CellView(r, c);
                cells[r][c] = cell;

                final int rr = r;
                final int cc = c;
                cell.setOnMouseClicked(e -> {
                    if (onCellClicked != null) onCellClicked.accept(rr, cc);
                });

                add(cell, c, r);
            }
        }
    }

    public void setOnCellClicked(BiConsumer<Integer, Integer> handler) {
        this.onCellClicked = handler;
    }

    public void render(BoardSnapshot s) {
        // очистим фишки
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                cells[r][c].setPiece(null);
            }
        }

        int[] w = s.whiteCols();
        int[] b = s.blackCols();

        for (int r = 0; r < rows; r++) {
            int wc = w[r];
            int bc = b[r];
            if (inBounds(r, wc)) cells[r][wc].setPiece(Player.WHITE);
            if (inBounds(r, bc)) cells[r][bc].setPiece(Player.BLACK);
        }
    }

    public void clearMarks() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                cells[r][c].clearMarks();
            }
        }
    }

    public void markSelected(int row, int col) {
        if (inBounds(row, col)) cells[row][col].setSelected(true);
    }

    public void markCandidate(int row, int col) {
        if (inBounds(row, col)) cells[row][col].setCandidate(true);
    }

    public void markTarget(int row, int col) {
        if (inBounds(row, col)) cells[row][col].setTarget(true);
    }

    private boolean inBounds(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }
}
