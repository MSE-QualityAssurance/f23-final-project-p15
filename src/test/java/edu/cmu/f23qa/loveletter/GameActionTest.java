package edu.cmu.f23qa.loveletter;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.security.Guard;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    private Deck mockDeck;
    private PlayerList mockPlayers;

    @BeforeEach
    public void setUp() {
        // Redirection output string
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
        mockDeck = mock(Deck.class);
        mockPlayers = mock(PlayerList.class);
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
        assertTrue(output.contains("P1\nP2\nP1 shows you a " + Card.GUARD + "\n"));
        //assertEquals(output, "P1\nP2\nP1 shows you a " + Card.GUARD + "\n");
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
        assertTrue(output.contains("P1\nP2\nP2 shows you a " + Card.BARON + "\n"));
        //assertEquals(output, "P1\nP2\nP2 shows you a " + Card.BARON + "\n");
    }

    /**
     * Test for the Bishop when the user guesses wrong
     */    
    @Test
    public void testUserBishopGuessedWrong() {
        when(mockHand2.peek(0)).thenReturn(Card.BARON);
        int guessedNum = 0;
        gameActions.useBishop(player1, guessedNum, player2, mockReader, mockDeck, mockPlayers);

        String output = outputStream.toString();
        assertTrue(output.contains("You have guessed incorrectly."));
    }

    /**
     * Test for the Bishop when the user guesses right and yet no one wins
     */
    @Test
    public void testUserBishopGuessedRight() {
        when(mockHand2.peek(0)).thenReturn(Card.BARON);
        int guessedNum = 3; 

        gameActions.useBishop(player1, guessedNum, player2, mockReader, mockDeck, mockPlayers);
        assertEquals(1, player1.getTokens());
    }

    /**
     * Test for the Bishop when the user guesses right and wins
     */
    @Test
    public void testUserBishopGuessedRightAndWins() {
        when(mockHand2.peek(0)).thenReturn(Card.BARON);
        int guessedNum = 3; 
        // make sure player1 has 3 tokens already
        player1.addToken();
        player1.addToken();
        player1.addToken();

        gameActions.useBishop(player1, guessedNum, player2, mockReader, mockDeck, mockPlayers);
        assertEquals(4, player1.getTokens());
        String output = outputStream.toString();
        assertTrue(output.contains("You have won the game!"));
    }

    /**
     * Test for the Bishop when the user guesses right 
     * and opponent chooses not to discard or draw a new card
     */
    @Test
    public void testUserBishopOpponentNotDraw() {
        when(mockHand2.peek(0)).thenReturn(Card.BARON);
        int guessedNum = 3; 

        when(mockReader.drawWhenBishop()).thenReturn(1);
        gameActions.useBishop(player1, guessedNum, player2, mockReader, mockDeck, mockPlayers);

        // assert that baron is still in mockhand2
        assertEquals(Card.BARON, mockHand2.peek(0));
    }

    /**
     * Test for the Bishop when the user guesses right 
     * and opponent chooses to discard and draw a new card
     * and there are not enough cards in deck
     */
    @Test
    public void testUserBishopOpponentDraw() {
        when(mockHand2.peek(0)).thenReturn(Card.BARON);
        int guessedNum = 3; 

        when(mockReader.drawWhenBishop()).thenReturn(0);
        when(mockDeck.hasMoreCards()).thenReturn(true);

        gameActions.useBishop(player1, guessedNum, player2, mockReader, mockDeck, mockPlayers);

        String output = outputStream.toString();
        assertTrue(output.contains("You have discarded a "+Card.BARON));
        assertTrue(output.contains("You have drawn a "+player2.getHand().peek(0)));
    }

    /**
     * Test for the Bishop when the user guesses right 
     * and opponent chooses to discard and draw a new card
     * and there are not enough cards in deck
     */
    @Test
    public void testUserBishopOpponentDrawDeckEmpty() {
        when(mockHand2.peek(0)).thenReturn(Card.BARON);
        int guessedNum = 3; 

        when(mockReader.drawWhenBishop()).thenReturn(0);
        when(mockDeck.hasMoreCards()).thenReturn(false);

        gameActions.useBishop(player1, guessedNum, player2, mockReader, mockDeck, mockPlayers);

        String output = outputStream.toString();
        assertTrue(output.contains("The deck is empty! You are out for this round!"));
    }

    /**
     * Test for the Queen
     * player1 wins the game and player2 is out
     */
    @Test
    public void testUserQueenWin() {
        Hand hand1 = new Hand(new ArrayList<>(Arrays.asList(Card.PRIEST)));
        Hand hand2 = new Hand(new ArrayList<>(Arrays.asList(Card.BARON)));

        Player spyPlayer1 = new Player("spyPlayer1", hand1, new DiscardPile());
        Player spyPlayer2 = new Player("spyPlayer2", hand2, new DiscardPile());

        gameActions.useQueen(spyPlayer1, spyPlayer2);

        assertTrue(spyPlayer1.isAlive() == true);
        assertTrue(spyPlayer2.isAlive() == false);

        String output = outputStream.toString();
        assertTrue(output.contains("You have won the comparison"));
    }

    /**
     * Test for the Queen
     * player2 wins the game and player1 is out
     */
    @Test
    public void testUserQueenLost() {
        Hand hand1 = new Hand(new ArrayList<>(Arrays.asList(Card.BARON)));
        Hand hand2 = new Hand(new ArrayList<>(Arrays.asList(Card.PRIEST)));

        Player spyPlayer1 = new Player("spyPlayer1", hand1, new DiscardPile());
        Player spyPlayer2 = new Player("spyPlayer2", hand2, new DiscardPile());

        gameActions.useQueen(spyPlayer1, spyPlayer2);

        assertTrue(spyPlayer1.isAlive() == false);
        assertTrue(spyPlayer2.isAlive() == true);
        String output = outputStream.toString();
        assertTrue(output.contains("You have lost the comparison!"));
    }

    /**
     * Test for the Queen
     * player1 and player2 tie, they both survive and nothing happens
     */
    @Test
    public void testUserQueenTie() {
        Hand hand1 = new Hand(new ArrayList<>(Arrays.asList(Card.BARON)));
        Hand hand2 = new Hand(new ArrayList<>(Arrays.asList(Card.BARON)));

        Player spyPlayer1 = new Player("spyPlayer1", hand1, new DiscardPile());
        Player spyPlayer2 = new Player("spyPlayer2", hand2, new DiscardPile());

        gameActions.useQueen(spyPlayer1, spyPlayer2);

        assertTrue(spyPlayer1.isAlive() == true);
        assertTrue(spyPlayer2.isAlive() == true);
    }
}
