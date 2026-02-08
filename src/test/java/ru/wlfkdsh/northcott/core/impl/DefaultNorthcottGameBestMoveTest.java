package ru.wlfkdsh.northcott.core.impl;

import org.junit.jupiter.api.Test;
import ru.wlfkdsh.northcott.core.api.Move;
import ru.wlfkdsh.northcott.core.api.Player;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DefaultNorthcottGameBestMoveTest {

    @Test
    void bestMoveEmptyWhenNimSumIsZero() {
        DefaultNorthcottGame game = new DefaultNorthcottGame(); // 8x8
        assertEquals(0, game.nimSum());
        assertTrue(game.bestMove().isEmpty(), "For nimSum=0 there is no guaranteed winning move");
    }

    @Test
    void bestMoveForWhiteWinsImmediatelyOn1x5() {
        // 1x5: WHITE=1, BLACK=4, gap=2, nimSum=2 != 0 => у WHITE есть выигрышный ход
        DefaultNorthcottGame game = new DefaultNorthcottGame(1, 5);

        assertEquals(Player.WHITE, game.currentPlayer());
        assertEquals(2, game.nimSum());

        Optional<Move> opt = game.bestMove();
        assertTrue(opt.isPresent());

        Move m = opt.get();
        assertEquals(Player.WHITE, m.player());
        assertEquals(0, m.row());
        assertEquals(1, m.fromCol());
        assertEquals(3, m.toCol(), "Winning move must close the gap to 0");

        assertTrue(game.makeMove(m.row(), m.toCol()));
        assertTrue(game.isGameOver());
        assertEquals(Player.WHITE, game.winner());
    }

    @Test
    void bestMoveForBlackWinsAfterWhiteMistakeOn1x5() {
        DefaultNorthcottGame game = new DefaultNorthcottGame(1, 5);

        // WHITE делает невыигрышный ход (оставляет gap=1)
        assertTrue(game.makeMove(0, 2));
        assertEquals(Player.BLACK, game.currentPlayer());
        assertEquals(1, game.nimSum());

        Optional<Move> opt = game.bestMove();
        assertTrue(opt.isPresent());

        Move m = opt.get();
        assertEquals(Player.BLACK, m.player());
        assertEquals(0, m.row());
        assertEquals(4, m.fromCol());
        assertEquals(3, m.toCol());

        assertTrue(game.makeMove(m.row(), m.toCol()));
        assertTrue(game.isGameOver());
        assertEquals(Player.BLACK, game.winner());
    }
}
