package edu.cmu.f23qa.loveletter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;

public class CardTest {

    // to learn more about JUnit 5: https://junit.org/junit5/docs/current/user-guide/#writing-tests
    @Test
    void testExpectedValues() {
        assertEquals(Card.GUARD.value(), 1);
    }

    /*
     * After drawing, I discard the Baron and choose  [normal player]. 
     * My card is a Priest, and  [normal player] is a Guard. 
     * Since my card has a higher value, the [normal player] is knocked out of the round.
     */
    @Test
    void testBaronUserWins(){
        Player user = new Player("User");
        user.getHand().add(Card.PRIEST);
        Player opponent = new Player("Opponent");
        opponent.getHand().add(Card.GUARD);
        GameActions gameActions = new GameActions() {}; 
        gameActions.useBaron(user, opponent);
        Assertions.assertTrue(opponent.getHand().hasCards() == false, 
            "Opponent should be eliminated");
    }

    /*
     * After drawing, I discard the Baron and choose [normal player].
     * My card is a Priest, and  [normal player] is a King. Since the 
     * [normal player] card has a higher value, I am knocked out of the round.
     */
    @Test
    void testBaronOpponentWins(){
        Player user = new Player("User");
        user.getHand().add(Card.PRIEST);
        Player opponent = new Player("Opponent");
        opponent.getHand().add(Card.KING);
        GameActions gameActions = new GameActions() {}; 
        gameActions.useBaron(user, opponent);
        Assertions.assertTrue(user.getHand().hasCards() == false, "User should be eliminated");
    }

    /*
     * I use the Baron and choose a player who also has a Baron. 
     * Since we both have cards of equal value, nothing happens, 
     * and no one is knocked out.
     */
    @Test
    void testBaronTies(){
        Player user = new Player("User");
        user.getHand().add(Card.HANDMAIDEN);
        Player opponent = new Player("Opponent");
        opponent.getHand().add(Card.HANDMAIDEN);
        GameActions gameActions = new GameActions() {}; 
        gameActions.useBaron(user, opponent);
        Assertions.assertTrue(user.getHand().hasCards()
            && opponent.getHand().hasCards(), "Nothing happens");    
    }

    /*
     * I cannot use the Baron on a [abnormal player] 
     * who is currently protected by a Handmaid.
     */
    @Test
    void testBaronProtected(){
        Player user = new Player("User");
        user.getHand().add(Card.KING);
        Player opponent = new Player("Opponent");
        opponent.getHand().add(Card.KING);
        opponent.switchProtection();
        GameActions gameActions = new GameActions() {}; 
        gameActions.useBaron(user, opponent);
        Assertions.assertTrue(user.getHand().hasCards() == true 
            && opponent.getHand().hasCards() == true, "Nothing happens");    
    }
}
