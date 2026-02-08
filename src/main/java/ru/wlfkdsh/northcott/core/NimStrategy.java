package ru.wlfkdsh.northcott.core;

import java.util.List;
import java.util.Optional;

public final class NimStrategy {

    public Optional<Move> findWinningMove(NorthcottBoard board, Player player) {
        if (board == null) throw new IllegalArgumentException("board == null");
        if (player == null) throw new IllegalArgumentException("player == null");

        int xor = board.nimSum();
        if (xor == 0) return Optional.empty();

        for (int r = 0; r < board.rows(); r++) {
            int g = board.gap(r);
            int target = g ^ xor;

            if (target < g) {
                int reduceBy = g - target;
                if (reduceBy <= 0) continue;

                if (player == Player.LEFT) {
                    int newL = board.leftCol(r) + reduceBy;
                    Move m = new Move(player, r, newL);
                    if (board.isLegal(m)) return Optional.of(m);
                } else {
                    int newR = board.rightCol(r) - reduceBy;
                    Move m = new Move(player, r, newR);
                    if (board.isLegal(m)) return Optional.of(m);
                }
            }
        }

        return Optional.empty();
    }

    public Move chooseMove(NorthcottBoard board, Player player) {
        return findWinningMove(board, player).orElseGet(() -> {
            List<Move> moves = board.legalMoves(player);
            if (moves.isEmpty()) throw new IllegalStateException("No moves available for " + player);
            return moves.get(0);
        });
    }
}
