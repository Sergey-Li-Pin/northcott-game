package ru.wlfkdsh.northcott.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class NorthcottBoard {
    private final int rows;
    private final int cols;
    private final int[] left;
    private final int[] right;

    public NorthcottBoard(int rows, int cols, int[] leftCols, int[] rightCols) {
        if (rows <= 0) throw new IllegalArgumentException("rows must be > 0");
        if (cols <= 1) throw new IllegalArgumentException("cols must be > 1");
        if (leftCols == null || rightCols == null) throw new IllegalArgumentException("arrays must not be null");
        if (leftCols.length != rows || rightCols.length != rows) {
            throw new IllegalArgumentException("arrays length must equal rows");
        }

        this.rows = rows;
        this.cols = cols;
        this.left = Arrays.copyOf(leftCols, rows);
        this.right = Arrays.copyOf(rightCols, rows);

        for (int r = 0; r < rows; r++) {
            validateRow(r);
        }
    }

    public static NorthcottBoard default3x6() {
        // Пример стартовой позиции 3x6:
        // row0: 1L3R0 => L=1, R=5, gap=3
        // row1: 0L1R3 => L=0, R=2, gap=1
        // row2: 1L2R1 => L=1, R=4, gap=2
        return new NorthcottBoard(3, 6, new int[]{1, 0, 1}, new int[]{5, 2, 4});
    }

    public int rows() { return rows; }
    public int cols() { return cols; }

    public int leftCol(int row) {
        checkRow(row);
        return left[row];
    }

    public int rightCol(int row) {
        checkRow(row);
        return right[row];
    }

    public int gap(int row) {
        checkRow(row);
        return right[row] - left[row] - 1;
    }

    public int nimSum() {
        int x = 0;
        for (int r = 0; r < rows; r++) {
            x ^= gap(r);
        }
        return x;
    }

    public boolean hasAnyMove() {
        for (int r = 0; r < rows; r++) {
            if (gap(r) > 0) return true;
        }
        return false;
    }

    public List<Move> legalMoves(Player player) {
        if (player == null) throw new IllegalArgumentException("player == null");

        List<Move> moves = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            int l = left[r];
            int rt = right[r];

            if (rt - l <= 1) continue; // gap == 0

            if (player == Player.LEFT) {
                for (int newL = l + 1; newL < rt; newL++) {
                    moves.add(new Move(player, r, newL));
                }
            } else {
                for (int newR = rt - 1; newR > l; newR--) {
                    moves.add(new Move(player, r, newR));
                }
            }
        }
        return moves;
    }

    public boolean isLegal(Move m) {
        if (m == null) return false;
        if (m.row() < 0 || m.row() >= rows) return false;
        if (m.newCol() < 0 || m.newCol() >= cols) return false;

        int r = m.row();
        int l = left[r];
        int rt = right[r];

        if (m.player() == Player.LEFT) {
            return m.newCol() > l && m.newCol() < rt;
        }
        if (m.player() == Player.RIGHT) {
            return m.newCol() < rt && m.newCol() > l;
        }
        return false;
    }

    public void apply(Move m) {
        if (!isLegal(m)) {
            throw new IllegalArgumentException("Illegal move: " + m);
        }
        int r = m.row();
        if (m.player() == Player.LEFT) {
            left[r] = m.newCol();
        } else {
            right[r] = m.newCol();
        }
        validateRow(r);
    }

    public NorthcottBoard copy() {
        return new NorthcottBoard(rows, cols, left, right);
    }

    private void validateRow(int r) {
        int l = left[r];
        int rt = right[r];
        if (l < 0 || l >= cols) throw new IllegalArgumentException("left[" + r + "] out of bounds: " + l);
        if (rt < 0 || rt >= cols) throw new IllegalArgumentException("right[" + r + "] out of bounds: " + rt);
        if (l >= rt) throw new IllegalArgumentException("left[" + r + "] must be < right[" + r + "]");
    }

    private void checkRow(int row) {
        if (row < 0 || row >= rows) throw new IndexOutOfBoundsException("row=" + row);
    }
}
