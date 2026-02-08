package ru.wlfkdsh.northcott.core.impl;

import ru.wlfkdsh.northcott.core.api.BoardSnapshot;
import ru.wlfkdsh.northcott.core.api.GameApi;
import ru.wlfkdsh.northcott.core.api.Move;
import ru.wlfkdsh.northcott.core.api.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class DefaultNorthcottGame implements GameApi {

    private static final int DEFAULT_ROWS = 8;
    private static final int DEFAULT_COLS = 8;

    private final int rows;
    private final int cols;

    private final int[] whiteCols;
    private final int[] blackCols;

    private Player current = Player.WHITE;

    private boolean gameOver = false;
    private Player winner = null;

    // ✅ Дефолтный конструктор под “классическую” доску 8×8
    public DefaultNorthcottGame() {
        this(DEFAULT_ROWS, DEFAULT_COLS);
    }

    public DefaultNorthcottGame(int rows, int cols) {
        if (rows <= 0) throw new IllegalArgumentException("rows must be > 0");
        if (cols <= 1) throw new IllegalArgumentException("cols must be > 1");

        this.rows = rows;
        this.cols = cols;
        this.whiteCols = new int[rows];
        this.blackCols = new int[rows];

        reset();
    }

    @Override
    public int rows() {
        return rows;
    }

    @Override
    public int cols() {
        return cols;
    }

    @Override
    public Player currentPlayer() {
        return current;
    }

    @Override
    public BoardSnapshot snapshot() {
        return new BoardSnapshot(rows, cols, whiteCols, blackCols);
    }

    @Override
    public boolean isGameOver() {
        return gameOver;
    }

    @Override
    public Player winner() {
        return winner;
    }

    @Override
    public int nimSum() {
        int s = 0;
        for (int r = 0; r < rows; r++) {
            int gap = blackCols[r] - whiteCols[r] - 1;
            if (gap > 0) s ^= gap;
        }
        return s;
    }

    @Override
    public List<Integer> legalDestinations(int row) {
        if (row < 0 || row >= rows) return List.of();
        if (gameOver) return List.of();

        int w = whiteCols[row];
        int b = blackCols[row];

        var out = new ArrayList<Integer>();

        // Учебный монотонный вариант:
        // WHITE двигается вправо (к BLACK), BLACK — влево (к WHITE).
        if (current == Player.WHITE) {
            for (int c = w + 1; c <= b - 1; c++) out.add(c);
        } else {
            for (int c = b - 1; c >= w + 1; c--) out.add(c);
        }

        return out;
    }

    @Override
    public boolean makeMove(int row, int toCol) {
        if (gameOver) return false;
        if (row < 0 || row >= rows) return false;

        var legal = legalDestinations(row);
        if (legal.isEmpty()) return false;
        if (!legal.contains(toCol)) return false;

        if (current == Player.WHITE) {
            whiteCols[row] = toCol;
        } else {
            blackCols[row] = toCol;
        }

        current = current.opposite();

        boolean hasMoves = false;
        for (int r = 0; r < rows; r++) {
            if (!legalDestinations(r).isEmpty()) { hasMoves = true; break; }
        }
        if (!hasMoves) {
            gameOver = true;
            winner = current.opposite();
        }

        return true;
    }

    @Override
    public Optional<Move> bestMove() {
        if (gameOver) return Optional.empty();

        int s = nimSum();
        if (s == 0) return Optional.empty();

        for (int r = 0; r < rows; r++) {
            int w = whiteCols[r];
            int b = blackCols[r];
            int gap = b - w - 1;
            if (gap <= 0) continue;

            int targetGap = gap ^ s;
            if (targetGap < gap) {
                int delta = gap - targetGap;

                if (current == Player.WHITE) {
                    int toCol = w + delta;
                    return Optional.of(new Move(current, r, w, toCol));
                } else {
                    int toCol = b - delta;
                    return Optional.of(new Move(current, r, b, toCol));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public void reset() {
        // ✅ Новая стартовая расстановка “как на картинке, но слева/справа”
        // Идея: WHITE занимает 2 крайних столбца слева (0 и 1) “шашечкой”,
        // BLACK занимает 2 крайних столбца справа (cols-2 и cols-1) “шашечкой”.
        // В каждой строке ровно по одной фишке каждого цвета.

        if (cols < 4) {
            // Фолбек на маленькие доски: просто крайние столбцы
            for (int r = 0; r < rows; r++) {
                whiteCols[r] = 0;
                blackCols[r] = cols - 1;
            }
        } else {
            for (int r = 0; r < rows; r++) {
                if ((r & 1) == 0) {
                    whiteCols[r] = 1;
                    blackCols[r] = cols - 1;
                } else {
                    whiteCols[r] = 0;
                    blackCols[r] = cols - 2;
                }
            }
        }

        current = Player.WHITE;
        gameOver = false;
        winner = null;

        boolean hasMoves = false;
        for (int r = 0; r < rows; r++) {
            if (!legalDestinations(r).isEmpty()) { hasMoves = true; break; }
        }
        if (!hasMoves) {
            gameOver = true;
            winner = Player.BLACK;
        }
    }
}
