package edu.cmu.f23qa.loveletter;

import java.util.Collections;
import java.util.Stack;

public class Deck {
    private Stack<Card> deck;
    private Card initiallyRemovedCard;
    private boolean removedCardUsed = false;

    public Deck() {
        this.deck = new Stack<>();
    }

    public void build() {
        // get the number of players
        // if 2-4, 16 cards
        // if 5-8, 32 cards
        // get the number of inputted players
        

        for (int i = 0; i < 5; i++) {
            deck.push(Card.GUARD);
        }

        for (int i = 0; i < 2; i++) {
            deck.push(Card.PRIEST);
            deck.push(Card.BARON);
            deck.push(Card.HANDMAIDEN);
            deck.push(Card.PRINCE);
        }

        deck.push(Card.KING);
        deck.push(Card.COUNTESS);
        deck.push(Card.PRINCESS);
    }

    public void shuffle() {
        Collections.shuffle(deck);
    }

    public Card draw() {
        return deck.pop();
    }

    public void removeCards(int num) {
        // remove one card from deck, face down
        // and also keep a record of it
        initiallyRemovedCard = deck.pop();

        // when num == 2, remove 3 more cards and face up (add them to discards)
        if (num == 2) {
            System.out.println("Removed 3 additional cards when there are only 2 players: ");
            for (int i = 0; i < 3; i++) {
                System.out.println(deck.pop());
            }
        }
    }

    public boolean hasMoreCards() {
        return !deck.isEmpty();
    }

    /**
     * For Prince, if the deck is empty, we could add the unused removed card back to the deck.
     * @return true if the deck is not empty, or it's empty but the removed card has not been used yet; 
     * false if the deck is empty and the removed card has been used
     */
    public boolean hasMoreCardsForPrince() {
        if (deck.isEmpty() && !removedCardUsed) {
            removedCardUsed = true;
            deck.push(initiallyRemovedCard);
            return true;
        }
        else if (deck.isEmpty() && removedCardUsed) {
            return false;
        }
        else {
            return true;
        }
    }
}
