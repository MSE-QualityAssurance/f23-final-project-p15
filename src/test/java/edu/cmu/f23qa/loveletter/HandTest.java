package edu.cmu.f23qa.loveletter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HandTest {

    private Hand hand;

    @BeforeEach
    public void setUp() {
        hand = new Hand();
    }

    // Test to verify royaltyPos method returns -1 when there are no royal cards
    @Test
    public void testRoyaltyPosWithNoRoyalCards() {
        hand.add(Card.GUARD);
        assertEquals(-1, hand.royaltyPos());
    }

    // Test to verify royaltyPos method returns correct index with one royal card
    @Test
    public void testRoyaltyPosWithOneRoyalCard() {
        hand.add(Card.KING);
        assertEquals(0, hand.royaltyPos());
    }

    // Test to verify royaltyPos method returns index of first royal card among multiple cards
    @Test
    public void testRoyaltyPosWithMultipleCardsIncludingRoyal() {
        hand.add(Card.GUARD);
        hand.add(Card.PRINCE);
        hand.add(Card.GUARD);
        assertEquals(1, hand.royaltyPos());
    }
}
