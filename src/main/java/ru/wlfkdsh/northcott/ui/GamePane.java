package ru.wlfkdsh.northcott.ui;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import ru.wlfkdsh.northcott.core.api.BoardSnapshot;
import ru.wlfkdsh.northcott.core.api.Move;
import ru.wlfkdsh.northcott.core.api.Player;
import ru.wlfkdsh.northcott.core.impl.DefaultNorthcottGame;

import java.util.HashSet;
import java.util.Set;

public final class GamePane extends BorderPane {
    private static final int AI_DELAY_MS = 200;

    private final DefaultNorthcottGame game;

    private final Player human = Player.WHITE;
    private final Player computer = Player.BLACK;

    private final BoardView boardView;

    private final Label turnLabel = new Label();
    private final Label posLabel = new Label();
    private final Label winnerLabel = new Label();

    private final Set<Integer> candidateCols = new HashSet<>();

    private Integer selectedRow = null;
    private Integer selectedCol = null;

    private Integer targetCol = null;
    private Integer minCandidate = null;
    private Integer maxCandidate = null;

    private PauseTransition aiTimer;

    private final CheckBox humanFirstCheck = new CheckBox("Игрок ходит первым");

    public GamePane() {
        this.game = new DefaultNorthcottGame(8, 8);

        BoardSnapshot s = game.snapshot();
        this.boardView = new BoardView(s.rows(), s.cols());
        this.boardView.setOnCellClicked(this::handleClick);

        setPadding(new Insets(12));
        setCenter(boardView);

        humanFirstCheck.setSelected(true);

        var newGameBtn = new Button("Новая игра");
        newGameBtn.setOnAction(e -> {
            restart();
            requestFocus();
        });

        var computerBtn = new Button("Ход компьютера (" + computer + ")");
        computerBtn.setOnAction(e -> {
            doComputerMove();
            requestFocus();
        });

        var top = new HBox(10, newGameBtn, computerBtn, humanFirstCheck);
        top.setAlignment(Pos.CENTER_LEFT);
        top.setPadding(new Insets(0, 0, 10, 0));
        setTop(top);

        var bottom = new VBox(6, turnLabel, posLabel, winnerLabel);
        bottom.setAlignment(Pos.CENTER_LEFT);
        bottom.setPadding(new Insets(10, 0, 0, 0));
        setBottom(bottom);

        setFocusTraversable(true);
        addEventHandler(KeyEvent.KEY_PRESSED, this::handleKey);

        renderAll();
        Platform.runLater(this::requestFocus);
    }

    private void restart() {
        stopAiTimer();

        Player start = humanFirstCheck.isSelected() ? human : computer;
        game.reset(start);

        clearSelection();
        renderAll();

        maybeAutoComputerMove();
    }

    private void handleClick(int row, int col) {
        requestFocus();

        if (game.isGameOver()) return;
        if (game.currentPlayer() != human) return;

        BoardSnapshot s = game.snapshot();
        int wCol = s.whiteCols()[row];

        if (selectedRow == null) {
            if (col == wCol) {
                selectRow(row);
                renderAll();
            }
            return;
        }

        if (col == s.whiteCols()[row]) {
            selectRow(row);
            renderAll();
            return;
        }

        if (row == selectedRow && candidateCols.contains(col)) {
            boolean ok = game.makeMove(row, col);

            clearSelection();
            renderAll();

            if (ok) {
                maybeAutoComputerMove();
            } else {
                renderAll();
            }
        }
    }

    private void handleKey(KeyEvent e) {
        KeyCode code = e.getCode();

        if (code == KeyCode.N) {
            restart();
            e.consume();
            return;
        }

        // резерв: вручную дать компьютеру сделать ход
        if (code == KeyCode.C) {
            doComputerMove();
            e.consume();
            return;
        }

        if (game.isGameOver()) return;

        if (game.currentPlayer() != human) return;

        if (code == KeyCode.ESCAPE) {
            clearSelection();
            renderAll();
            e.consume();
            return;
        }

        if (code == KeyCode.UP) {
            moveSelectionRow(-1);
            e.consume();
            return;
        }

        if (code == KeyCode.DOWN) {
            moveSelectionRow(+1);
            e.consume();
            return;
        }

        if (code == KeyCode.LEFT) {
            moveTarget(-1);
            e.consume();
            return;
        }

        if (code == KeyCode.RIGHT) {
            moveTarget(+1);
            e.consume();
            return;
        }

        if (code == KeyCode.ENTER || code == KeyCode.SPACE) {
            commitKeyboardMove();
            e.consume();
        }
    }

    private void moveSelectionRow(int delta) {
        int rows = game.rows();
        int start = (selectedRow == null) ? 0 : selectedRow;

        for (int i = 0; i < rows; i++) {
            int r = (start + delta + rows) % rows;
            start = r;

            if (!game.legalDestinations(r).isEmpty()) {
                selectRow(r);
                renderAll();
                return;
            }
        }

        renderAll();
    }

    private void moveTarget(int delta) {
        if (selectedRow == null || targetCol == null || minCandidate == null || maxCandidate == null) return;

        int t = targetCol + delta;
        if (t < minCandidate) t = minCandidate;
        if (t > maxCandidate) t = maxCandidate;

        targetCol = t;
        renderAll();
    }

    private void commitKeyboardMove() {
        if (selectedRow == null) {
            moveSelectionRow(+1);
            return;
        }
        if (targetCol == null) return;

        boolean ok = game.makeMove(selectedRow, targetCol);

        clearSelection();
        renderAll();

        if (ok) {
            maybeAutoComputerMove();
        } else {
            renderAll();
        }
    }

    private void selectRow(int row) {
        BoardSnapshot s = game.snapshot();

        selectedRow = row;
        selectedCol = s.whiteCols()[row];

        rebuildCandidates(row);
    }

    private void rebuildCandidates(int row) {
        candidateCols.clear();

        var dests = game.legalDestinations(row);
        if (dests.isEmpty()) {
            minCandidate = null;
            maxCandidate = null;
            targetCol = null;
            return;
        }

        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (Integer d : dests) {
            candidateCols.add(d);
            if (d < min) min = d;
            if (d > max) max = d;
        }

        minCandidate = min;
        maxCandidate = max;

        if (targetCol == null || targetCol < minCandidate || targetCol > maxCandidate) {
            targetCol = minCandidate;
        }
    }

    private void maybeAutoComputerMove() {
        if (game.isGameOver()) return;
        if (game.currentPlayer() != computer) return;

        stopAiTimer();

        aiTimer = new PauseTransition(Duration.millis(AI_DELAY_MS));
        aiTimer.setOnFinished(ev -> {
            aiTimer = null;
            doComputerMove();
        });
        aiTimer.play();
    }

    private void stopAiTimer() {
        if (aiTimer != null) {
            aiTimer.stop();
            aiTimer = null;
        }
    }

    private void doComputerMove() {
        stopAiTimer();

        if (game.isGameOver()) return;
        if (game.currentPlayer() != computer) return;

        boolean moved = false;

        var best = game.bestMove();
        if (best.isPresent()) {
            Move m = best.get();
            moved = game.makeMove(m.row(), m.toCol());
        }

        if (!moved) {
            for (int r = 0; r < game.rows(); r++) {
                var dests = game.legalDestinations(r);
                if (!dests.isEmpty()) {
                    moved = game.makeMove(r, dests.get(0));
                    if (moved) break;
                }
            }
        }

        clearSelection();
        renderAll();
    }

    private void renderAll() {
        BoardSnapshot s = game.snapshot();

        boardView.render(s);
        boardView.clearMarks();

        if (selectedRow != null && selectedCol != null) {
            boardView.markSelected(selectedRow, selectedCol);
            for (Integer c : candidateCols) {
                boardView.markCandidate(selectedRow, c);
            }
            if (targetCol != null) {
                boardView.markTarget(selectedRow, targetCol);
            }
        }

        if (game.isGameOver()) {
            turnLabel.setText("Игра окончена.");
            Player w = game.winner();
            winnerLabel.setText(w != null ? ("Победитель: " + w) : "Победитель: —");
            posLabel.setText("");
            return;
        }

        turnLabel.setText("Ход: " + game.currentPlayer());

        int nim = game.nimSum();
        posLabel.setText(nim == 0
                ? "Позиция: проигрышная (nim=0)"
                : ("Позиция: выигрышная (nim=" + nim + ")"));

        winnerLabel.setText("");
    }

    private void clearSelection() {
        selectedRow = null;
        selectedCol = null;
        targetCol = null;
        minCandidate = null;
        maxCandidate = null;
        candidateCols.clear();
    }
}
