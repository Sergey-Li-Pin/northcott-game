package ru.wlfkdsh.northcott.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NimStrategyTest {

    @Test
    void winningMove_makesNimSumZero_forLeft() {
        NorthcottBoard b = new NorthcottBoard(3, 6, new int[]{0, 0, 0}, new int[]{5, 4, 3}); // gaps 4,3,2 => xor=5
        NimStrategy s = new NimStrategy();

        Move m = s.findWinningMove(b, Player.LEFT).orElseThrow();
        NorthcottBoard after = b.copy();
        after.apply(m);

        assertEquals(0, after.nimSum(), "After an optimal move nim-sum must be 0");
    }

    @Test
    void winningMove_makesNimSumZero_forRight() {
        NorthcottBoard b = new NorthcottBoard(3, 6, new int[]{0, 0, 0}, new int[]{5, 4, 3}); // gaps 4,3,2 => xor=5
        NimStrategy s = new NimStrategy();

        Move m = s.findWinningMove(b, Player.RIGHT).orElseThrow();
        NorthcottBoard after = b.copy();
        after.apply(m);

        assertEquals(0, after.nimSum(), "After an optimal move nim-sum must be 0");
    }

    @Test
    void noWinningMove_whenNimSumZero() {
        NorthcottBoard b = NorthcottBoard.default3x6(); // nimSum==0
        NimStrategy s = new NimStrategy();

        assertTrue(s.findWinningMove(b, Player.LEFT).isEmpty());
        assertTrue(s.findWinningMove(b, Player.RIGHT).isEmpty());
    }
}
