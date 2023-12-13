package edu.cmu.f23qa.loveletter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    private Player player1;
    private Player player2;

    @BeforeEach
    public void setUp() {
        player1 = new Player("Player1");
        player2 = new Player("Player2");
        player1.getHand().add(Card.GUARD);
        player2.getHand().add(Card.PRIEST);
    }

    // Test to ensure a player is correctly eliminated from the game
    @Test
    public void testEliminate() {
        player1.eliminate();
        assertFalse(player1.isAlive());
        assertEquals(Card.GUARD, player1.getDiscarded().getCards().get(0));
    }

    // Test to verify that switchHand correctly swaps the hands of two players
    @Test
    public void testSwitchHand() {
        player1.switchHand(player2);
        assertEquals(Card.PRIEST, player1.getHand().peek(0));
        assertEquals(Card.GUARD, player2.getHand().peek(0));
    }
}
