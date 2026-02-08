// src/main/java/ru/wlfkdsh/northcott/core/api/BoardSnapshot.java
package ru.wlfkdsh.northcott.core.api;

import java.util.Arrays;

public record BoardSnapshot(int rows, int cols, int[] whiteCols, int[] blackCols) {

    public BoardSnapshot {
        if (rows <= 0) throw new IllegalArgumentException("rows must be > 0");
        if (cols <= 1) throw new IllegalArgumentException("cols must be > 1");
        if (whiteCols == null || blackCols == null) throw new IllegalArgumentException("arrays are null");
        if (whiteCols.length != rows || blackCols.length != rows)
            throw new IllegalArgumentException("arrays length must be == rows");

        whiteCols = Arrays.copyOf(whiteCols, whiteCols.length);
        blackCols = Arrays.copyOf(blackCols, blackCols.length);
    }

    public int whiteCol(int row) {
        return whiteCols[row];
    }

    public int blackCol(int row) {
        return blackCols[row];
    }

    public int gap(int row) {
        return blackCols[row] - whiteCols[row] - 1;
    }
}
