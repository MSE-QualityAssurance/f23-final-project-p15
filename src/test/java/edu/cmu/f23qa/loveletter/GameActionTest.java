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
    private PlayerList mockPlayers;
    private Deck mockDeck;

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
        mockDeck = mock(Deck.class);
        mockPlayers = mock(PlayerList.class);
        mockDeck = mock (Deck.class);
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
    /**
     * Test for the effects of Priest
     * Ensures that the usePriest method correctly reveals the opponent's card.
     */ 
    @Test
    public void testUsePriest() {
        Card opponentCard = Card.PRIEST;
        when(mockHand2.peek(0)).thenReturn(opponentCard);
        Player opponent = new Player("Opponent", mockHand2, mockDiscardPile2);

        gameActions.usePriest(opponent);
        assertEquals("Opponent shows you a Priest (2)", outputStream.toString().trim());
    }

    /**
     * Test for the effects of Prince
     * Verifies that the usePrince method forces the opponent to discard their hand and draw a new card.
     */
    @Test
    public void testUsePrince() {
        when(mockDeck.hasMoreCardsWithExtraCard()).thenReturn(true);
        when(mockDeck.draw()).thenReturn(Card.GUARD);
        when(mockHand2.peek(0)).thenReturn(Card.PRINCESS);
        Player opponent = new Player("Opponent", mockHand2, mockDiscardPile2);

        gameActions.usePrince(opponent, mockDeck);

        verify(mockHand2, times(1)).remove(0);
        verify(mockHand2, times(1)).add(Card.GUARD);
    }

    /**
     * Test for the effects of King
     * Checks if the useKing method properly swaps the cards between the user and the opponent.
     */
    @Test
    public void testUseKing() {
        Hand userHand = new Hand();
        Hand opponentHand = new Hand();
        userHand.add(Card.KING); // Adding a KING card to the user's hand
        opponentHand.add(Card.PRIEST); // Adding a PRIEST card to the opponent's hand

        DiscardPile userDiscardPile = new DiscardPile();
        DiscardPile opponentDiscardPile = new DiscardPile();

        Player user = new Player("User", userHand, userDiscardPile);
        Player opponent = new Player("Opponent", opponentHand, opponentDiscardPile);

        GameActions gameActions = new GameActions() {};
        gameActions.useKing(user, opponent);

        assertEquals(Card.PRIEST, user.getHand().peek(0));
        assertEquals(Card.KING, opponent.getHand().peek(0));
    }
    
    /**
     * Test for the effects of Baroness inspecting one player
     * Confirms that the useBaroness method allows a player to inspect one opponent's hand.
     */
    @Test
    public void testUseBaronessInspectOnePlayer() {
        when(mockHand2.peek(0)).thenReturn(Card.KING);

        gameActions.useBaroness(player2, null);

        assertEquals("P2 shows you a King (6)", outputStream.toString().trim());
    }

    /**
     * Test for the effects of Baroness inspecting two players
     * Ensures that the useBaroness method allows a player to inspect two opponents' hands.
     */
    @Test
    public void testUseBaronessInspectTwoPlayers() {
        when(mockHand1.peek(0)).thenReturn(Card.KING);
        when(mockHand2.peek(0)).thenReturn(Card.PRINCESS);

        gameActions.useBaroness(player1, player2);

        assertEquals("P1 shows you a King (6)\nP2 shows you a Princess (8)", outputStream.toString().trim());
    }
}
