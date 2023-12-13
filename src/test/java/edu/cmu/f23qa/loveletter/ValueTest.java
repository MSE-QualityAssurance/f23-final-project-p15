package edu.cmu.f23qa.loveletter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ValueTest {

    private DiscardPile discardPile;

    @BeforeEach
    public void setUp() {
        discardPile = new DiscardPile();
    }

    @Test
    public void testValueWithNoCards() {
        assertEquals(0, discardPile.value(), "The value should be 0 when no cards are in the pile.");
    }

    @Test
    public void testValueWithOneCard() {
        discardPile.add(Card.PRINCE); // Assuming Card.PRINCE has a specific value
        assertEquals(Card.PRINCE.value(), discardPile.value(), "The value should equal the value of the single card in the pile.");
    }

    @Test
    public void testValueWithMultipleCards() {
        discardPile.add(Card.PRINCE);
        discardPile.add(Card.KING); // Add multiple cards
        int expectedValue = Card.PRINCE.value() + Card.KING.value();
        assertEquals(expectedValue, discardPile.value(), "The value should be the sum of the values of all cards in the pile.");
    }
}
