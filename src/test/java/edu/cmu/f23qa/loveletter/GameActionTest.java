package edu.cmu.f23qa.loveletter;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class GameActionTest {

    private ByteArrayOutputStream outputStream;

    private GameActions gameActions;
    private Hand mockHand1, mockHand2;
    private DiscardPile mockDiscardPile1, mockDiscardPile2;
    private Player player1, player2;
    private Reader mockReader;

    @BeforeEach
    public void setUp() {
        // Redirection output stream
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Create test doubles
        gameActions = new GameActions(){};
        mockHand1 = mock(Hand.class);
        mockHand2 = mock(Hand.class);
        mockDiscardPile1 = mock(DiscardPile.class);
        mockDiscardPile2 = mock(DiscardPile.class);
        player1 = new Player("P1", mockHand1, mockDiscardPile1);
        player2 = new Player("P2", mockHand2, mockDiscardPile2);
        mockReader = mock(Reader.class);
    }

    /**
     * Test for th effects of Guard
     * The user guesses the opponent's hand card correctly
     */
    @Test
    public void testUseGuardCorrectGuess(){
        // Define the behavior of test doubles
        Hand userHand = new Hand(new ArrayList<>(Arrays.asList(Card.GUARD)));
        Hand opponentHand = new Hand(new ArrayList<>(Arrays.asList(Card.PRIEST))); 
        DiscardPile userDiscardPile = Mockito.mock(DiscardPile.class);
        DiscardPile opponentDiscardPile = Mockito.mock(DiscardPile.class);
        Player user = new Player("User", userHand, userDiscardPile);
        Player opponent = new Player("Opponent", opponentHand, opponentDiscardPile);
        Deck deck = Mockito.mock(Deck.class);

        // Assert revealing card and output
        GameActions gameActions = new GameActions() {};
        gameActions.useGuard(2, opponent, user, deck); 

        Assertions.assertFalse(opponent.isAlive(), "Opponent should be eliminated for having a Priest");
    }

    /**
     * Test for the effects of Guard
     * The user guesses the opponent's hand card incorrectly
     */
    @Test
    void testGuardIncorrectGuess() {
        // Define the behavior of test doubles
        Hand userHand = new Hand(new ArrayList<>(Arrays.asList(Card.GUARD)));
        Hand opponentHand = new Hand(new ArrayList<>(Arrays.asList(Card.PRIEST))); 
        DiscardPile userDiscardPile = Mockito.mock(DiscardPile.class);
        DiscardPile opponentDiscardPile = Mockito.mock(DiscardPile.class);
        Player user = new Player("User", userHand, userDiscardPile);
        Player opponent = new Player("Opponent", opponentHand, opponentDiscardPile);
        Deck deck = Mockito.mock(Deck.class);

        // Assert revealing card and output
        GameActions gameActions = new GameActions() {};
        gameActions.useGuard(6, opponent, user, deck); 

        Assertions.assertTrue(opponent.isAlive(), "Opponent should not be eliminated for having a Priest");
    }

    /**
     * Test for the effects of Baron
     * The case that the user beats the oponent in card comparison
     */
    @Test
    void testBaronUserWins() {
        // Define the behavior of test doubles
        Hand userHand = new Hand(new ArrayList<>(Arrays.asList(Card.KING)));
        Hand opponentHand = new Hand(new ArrayList<>(Arrays.asList(Card.HANDMAIDEN)));
        DiscardPile userDiscardPile = Mockito.mock(DiscardPile.class);
        DiscardPile opponentDiscardPile = Mockito.mock(DiscardPile.class);
        Player user = new Player("User", userHand, userDiscardPile);
        Player opponent = new Player("Opponent", opponentHand, opponentDiscardPile);

        // Assert revealing card and output
        GameActions gameActions = new GameActions() {};
        gameActions.useBaron(user, opponent);

        Assertions.assertFalse(opponent.isAlive(), "Opponent should be eliminated");
    }

    /**
     * Test for the effects of Baron
     * The case that the opponent beats the user in card comparison
     */
    @Test
    void testBaronOpponentWins(){
        // Define the behavior of test doubles
        Hand userHand = new Hand(new ArrayList<>(Arrays.asList(Card.HANDMAIDEN)));
        Hand opponentHand = new Hand(new ArrayList<>(Arrays.asList(Card.KING)));
        DiscardPile userDiscardPile = Mockito.mock(DiscardPile.class);
        DiscardPile opponentDiscardPile = Mockito.mock(DiscardPile.class);
        Player user = new Player("User", userHand, userDiscardPile);
        Player opponent = new Player("Opponent", opponentHand, opponentDiscardPile);

        // Assert revealing card and output
        GameActions gameActions = new GameActions() {};
        gameActions.useBaron(user, opponent);

        Assertions.assertFalse(user.isAlive(), "User should be eliminated");
    }

    /**
     * Test for the effects of Baron
     * The case that the user and the oponent ties in card comparison
     */
    @Test
    void testBaronTiesMock(){
        // Define the behavior of test doubles
        Hand userHand = new Hand(new ArrayList<>(Arrays.asList(Card.KING)));
        Hand opponentHand = new Hand(new ArrayList<>(Arrays.asList(Card.KING)));
        DiscardPile userDiscardPile = Mockito.mock(DiscardPile.class);
        DiscardPile opponentDiscardPile = Mockito.mock(DiscardPile.class);
        Player user = new Player("User", userHand, userDiscardPile);
        Player opponent = new Player("Opponent", opponentHand, opponentDiscardPile);

        // Assert revealing card and output
        GameActions gameActions = new GameActions() {};
        gameActions.useBaron(user, opponent);
        Assertions.assertTrue(opponent.isAlive() && user.isAlive(), "Nothing happens");
    }

    /**
     * Test for the effects of Princess
     * The case that the user be eliminated for discarding Princess
     */
    @Test
    void testDiscardPrincessVoluntarily() {
        // Define the behavior of test doubles
        Hand userHand = new Hand(new ArrayList<>(Arrays.asList(Card.PRINCESS)));
        DiscardPile discardPile = Mockito.mock(DiscardPile.class);
        Player user = new Player("user", userHand, discardPile);

        // Assert revealing card and output
        GameActions gameActions = new GameActions() {};
        gameActions.usePrincess(user);

        Assertions.assertFalse(user.isAlive(), "User should be eliminated for discarding Princess");
    }

    /**
     * Test for the effects of Cardinal
     * The user will look at the first opponents' hand after they switch their hand
     */
    @Test
    public void testUseCardinalLookFirstOpponent(){
        // Define the behavior of test doubles
        when(mockHand1.peek(0)).thenReturn(Card.BARON);
        when(mockHand2.peek(0)).thenReturn(Card.GUARD);
        when(mockReader.choosePlayerWhenCardinal()).thenReturn(0);

        // Before useCardinal()
        assertEquals(player1.getHand().peek(0), Card.BARON);
        assertEquals(player2.getHand().peek(0), Card.GUARD);

        gameActions.useCardinal(player1, player2, mockReader);

        // Assert switching hand
        assertEquals(player1.getHand().peek(0), Card.GUARD);
        assertEquals(player2.getHand().peek(0), Card.BARON);

        // Assert revealing card and output
        String output = outputStream.toString();
        assertTrue(output.contains("P1 shows you a " + Card.GUARD));
    }

    /**
     * Test for the effects of Cardinal
     * The user will look at the second opponent's hand after they switch their hand
     */
    @Test
    public void testUseCardinalLookSecondOpponent(){
        // Define the behavior of test doubles
        when(mockHand1.peek(0)).thenReturn(Card.BARON);
        when(mockHand2.peek(0)).thenReturn(Card.GUARD);
        when(mockReader.choosePlayerWhenCardinal()).thenReturn(1);

        // Before useCardinal()
        assertEquals(player1.getHand().peek(0), Card.BARON);
        assertEquals(player2.getHand().peek(0), Card.GUARD);

        gameActions.useCardinal(player1, player2, mockReader);

        // Assert switching hand
        assertEquals(player1.getHand().peek(0), Card.GUARD);
        assertEquals(player2.getHand().peek(0), Card.BARON);

        // Assert revealing card and output
        String output = outputStream.toString();
        assertTrue(output.contains("P2 shows you a " + Card.BARON));
    }

}
