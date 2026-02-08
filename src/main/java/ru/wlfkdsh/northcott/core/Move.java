package ru.wlfkdsh.northcott.core;

public record Move(Player player, int row, int newCol) {
    public Move {
        if (player == null) throw new IllegalArgumentException("player == null");
        if (row < 0) throw new IllegalArgumentException("row < 0");
        if (newCol < 0) throw new IllegalArgumentException("newCol < 0");
    }
}
