package ru.wlfkdsh.northcott.core;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NorthcottBoardTest {

    @Test
    void defaultBoard_hasExpectedGapsAndNimSum() {
        NorthcottBoard b = NorthcottBoard.default3x6();

        assertEquals(3, b.rows());
        assertEquals(6, b.cols());

        assertEquals(3, b.gap(0));
        assertEquals(1, b.gap(1));
        assertEquals(2, b.gap(2));

        assertEquals(0, b.nimSum(), "3 XOR 1 XOR 2 must be 0");
    }

    @Test
    void legalMoves_generatedCorrectly() {
        NorthcottBoard b = new NorthcottBoard(1, 6, new int[]{1}, new int[]{5}); // gap=3

        List<Move> leftMoves = b.legalMoves(Player.LEFT);
        assertEquals(3, leftMoves.size());
        assertTrue(leftMoves.contains(new Move(Player.LEFT, 0, 2)));
        assertTrue(leftMoves.contains(new Move(Player.LEFT, 0, 3)));
        assertTrue(leftMoves.contains(new Move(Player.LEFT, 0, 4)));

        List<Move> rightMoves = b.legalMoves(Player.RIGHT);
        assertEquals(3, rightMoves.size());
        assertTrue(rightMoves.contains(new Move(Player.RIGHT, 0, 4)));
        assertTrue(rightMoves.contains(new Move(Player.RIGHT, 0, 3)));
        assertTrue(rightMoves.contains(new Move(Player.RIGHT, 0, 2)));
    }

    @Test
    void apply_updatesPositionsAndGap() {
        NorthcottBoard b = new NorthcottBoard(1, 6, new int[]{1}, new int[]{5}); // gap=3

        b.apply(new Move(Player.LEFT, 0, 4));
        assertEquals(0, b.gap(0));

        assertEquals(4, b.leftCol(0));
        assertEquals(5, b.rightCol(0));

        assertTrue(b.legalMoves(Player.LEFT).isEmpty());
        assertTrue(b.legalMoves(Player.RIGHT).isEmpty());
    }

    @Test
    void illegalMove_throws() {
        NorthcottBoard b = new NorthcottBoard(1, 6, new int[]{1}, new int[]{5});
        assertThrows(IllegalArgumentException.class, () -> b.apply(new Move(Player.LEFT, 0, 5)));
        assertThrows(IllegalArgumentException.class, () -> b.apply(new Move(Player.RIGHT, 0, 1)));
    }
}
