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
        player1.getHand().add(Card.GUARD); // Assuming Card.GUARD is a valid card
        player2.getHand().add(Card.PRIEST); // Assuming Card.PRIEST is a valid card
    }

    @Test
    public void testEliminate() {
        player1.eliminate();
        assertFalse(player1.isAlive(), "Player should be eliminated (hand should be empty)");
        assertEquals(Card.GUARD, player1.getDiscarded().getCards().get(0), "The discarded card should be GUARD");
    }

    @Test
    public void testSwitchHand() {
        player1.switchHand(player2);
        assertEquals(Card.PRIEST, player1.getHand().peek(0), "Player1 should now have the PRIEST card");
        assertEquals(Card.GUARD, player2.getHand().peek(0), "Player2 should now have the GUARD card");
    }
}
