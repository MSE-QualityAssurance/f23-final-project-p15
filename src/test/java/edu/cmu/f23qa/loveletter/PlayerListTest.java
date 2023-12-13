package edu.cmu.f23qa.loveletter;

import java.util.*;

import org.easymock.Mock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


public class PlayerListTest {
    
    /**
     * Test for player numbers between 2-8
     */
    @Test
    public void testSetTokensToWinValidPlayers() {
        // Create test double and define its behavior
        Player mockPlayer1 = mock(Player.class);
        Player mockPlayer2 = mock(Player.class);
        Player mockPlayer3 = mock(Player.class);

        // Create input
        LinkedList<Player> players = new LinkedList<>(Arrays.asList(mockPlayer1, mockPlayer2, mockPlayer3));

         // Verify results
        PlayerList playerList = new PlayerList(players);
        Assertions.assertEquals(playerList.setTokensToWin(), 5);
    }

    /**
     * Test for player numbers below 1 or above 9
     */
    @Test
    public void testSetTokensToWinInvalidPlayers() {
        // Create test double and define its behavior
        Player mockPlayer1 = mock(Player.class);

        // Create input
        LinkedList<Player> players = new LinkedList<>(Arrays.asList(mockPlayer1));
        PlayerList playerList = new PlayerList(players);
        
         // Verify results
        Assertions.assertEquals(playerList.setTokensToWin(), 0);
    }

    /**
     * Test for adding a list of player names
     */
    @Test
    public void testAddPlayers() {
        // Create test double and define its behavior
        List<String> playerNames = new ArrayList<>(Arrays.asList("u1", "u2", "u3"));
        Player player1 = new Player("u1");
        Player player2 = new Player("u2");
        Player player3 = new Player("u3");

        // Create input
        LinkedList<Player> expectedPlayers = new LinkedList<>(Arrays.asList(player1, player2, player3));
        LinkedList<Player> testPlayers = new LinkedList<>();
        PlayerList playerList = new PlayerList(testPlayers);

         // Verify results
        playerList.addPlayers(playerNames);
        for (int i = 0; i < testPlayers.size(); i++){
            Assertions.assertEquals(testPlayers.get(i).getName(), expectedPlayers.get(i).getName());
        }

        Assertions.assertEquals(playerList, playerList);
    }

    /**
     * Test for getting current player and update on currentplayer index
     */
    @Test
    public void testGetCurrentPlayer() {
        // Create test double and define its behavior
        Player mockPlayer1 = mock(Player.class);
        Player mockPlayer2 = mock(Player.class);
        Player mockPlayer3 = mock(Player.class);

        // Create input
        LinkedList<Player> players = new LinkedList<>(Arrays.asList(mockPlayer1, mockPlayer2, mockPlayer3));
        PlayerList playerList = new PlayerList(players);

         // Verify results
        playerList.currentPlayer = 2;
        Assertions.assertEquals(playerList.getCurrentPlayer(), mockPlayer3);
        Assertions.assertEquals(playerList.currentPlayer, 0);
    }

    /**
     * Test for getting all alive players in the game
     */
    @Test
    public void testGetAlivePlayer() {
        // Create test double and define its behavior
        Player mockPlayer1 = mock(Player.class);
        Player mockPlayer2 = mock(Player.class);
        Player mockPlayer3 = mock(Player.class);

        // Create input
        when(mockPlayer1.isAlive()).thenReturn(true);
        when(mockPlayer2.isAlive()).thenReturn(false);
        when(mockPlayer3.isAlive()).thenReturn(true);

         // Verify results
        LinkedList<Player> players = new LinkedList<>(Arrays.asList(mockPlayer1, mockPlayer2, mockPlayer3));
        PlayerList playerList = new PlayerList(players);
        List<Player> expectedWinners = new ArrayList<>(Arrays.asList(mockPlayer1, mockPlayer3));
        Assertions.assertEquals(playerList.getAlivePlayers(), expectedWinners);
    }

    /**
     * Test for getting all get winners for the game
     */
    @Test
    public void testGetWinner() {
        // Create test double and define its behavior
        Player mockPlayer1 = mock(Player.class);
        Player mockPlayer2 = mock(Player.class);
        Player mockPlayer3 = mock(Player.class);

        // Create input
        when(mockPlayer1.getTokens()).thenReturn(5);
        when(mockPlayer2.getTokens()).thenReturn(5);
        when(mockPlayer3.getTokens()).thenReturn(4);

         // Verify results
        LinkedList<Player> players = new LinkedList<>(Arrays.asList(mockPlayer1, mockPlayer2, mockPlayer3));
        PlayerList playerList = new PlayerList(players);
        List<Player> expectedWinners = new ArrayList<>(Arrays.asList(mockPlayer1, mockPlayer2));
        Assertions.assertEquals(playerList.getGameWinner(), expectedWinners);
    }

    /**
     * Test for dealing card for each user in a round
     */
    @Test
    public void testDealCards() {
        // Create test double and define its behavior
        Stack<Card> cards = new Stack<>();
        cards.add(Card.KING);
        cards.add(Card.ASSASIN);
        cards.add(Card.BARON);
        Deck deck = new Deck(cards);
        Hand hand1 = new Hand(new ArrayList<>());
        Hand hand2 = new Hand(new ArrayList<>());
        Hand hand3 = new Hand(new ArrayList<>());

        // Create input
        Player mockPlayer1 = mock(Player.class);
        Player mockPlayer2 = mock(Player.class);
        Player mockPlayer3 = mock(Player.class);
        when(mockPlayer1.getHand()).thenReturn(hand1);
        when(mockPlayer2.getHand()).thenReturn(hand2);
        when(mockPlayer3.getHand()).thenReturn(hand3);

         // Verify results
        LinkedList<Player> players = new LinkedList<>(Arrays.asList(mockPlayer1, mockPlayer2, mockPlayer3));
        PlayerList playerList = new PlayerList(players);
        Hand expecteHand = new Hand(new ArrayList<>(Arrays.asList(Card.BARON)));
        playerList.dealCards(deck);
        Assertions.assertEquals(mockPlayer1.getHand().peek(0), expecteHand.peek(0));       
    }

    /**
     * Test for checking the number of card count in discard pile of a player
     */
    @Test
    public void testCheckCountInDiscard() {
        // Create test double and define its behavior
        ArrayList<Card> cards = new ArrayList<>(Arrays.asList(Card.KING, Card.COUNT));
        DiscardPile discardPile = new DiscardPile(cards);

        // Create input        
        Hand hand = mock(Hand.class);
        Player player = new Player("u1", hand, discardPile);

         // Verify results
        LinkedList<Player> players = new LinkedList<>(Arrays.asList(player));
        PlayerList playerList = new PlayerList(players);
        Assertions.assertEquals(playerList.checkCountInDiscard(player), 1);
    }

    /**
     * Test for only one player has the highest discard pile value
     */
    @Test
    public void testCompareUsedPilesOneWinner(){
        // Create test double and define its behavior
        Hand spyHand = spy(Hand.class);
        DiscardPile spyDiscardPile1 = spy(DiscardPile.class);
        DiscardPile spyDiscardPile2 = spy(DiscardPile.class);
        DiscardPile spyDiscardPile3 = spy(DiscardPile.class);
        when(spyDiscardPile1.value()).thenReturn(2);
        when(spyDiscardPile2.value()).thenReturn(5);
        when(spyDiscardPile3.value()).thenReturn(10);

        // Create input
        Player p1 = new Player("P1", spyHand, spyDiscardPile1);
        Player p2 = new Player("P2", spyHand, spyDiscardPile2);
        Player p3 = new Player("P3", spyHand, spyDiscardPile3);
        List<Player> winnersAfterCmpHands = new ArrayList<>(Arrays.asList(p1, p2, p3));
        PlayerList playerList = new PlayerList();

        // Verify results
        List<Player> winners = playerList.compareUsedPiles(winnersAfterCmpHands);
        assertEquals(winners.size(), 1);
        assertEquals(winners.get(0), p3);
    }

    /**
     * Test for more than one players has the equally highest discard pile value (tie)
     */
    @Test
    public void testCompareUsedPilesMultiWinners(){
        // Create test double and define its behavior
        Hand spyHand = spy(Hand.class);
        DiscardPile spyDiscardPile1 = spy(DiscardPile.class);
        DiscardPile spyDiscardPile2 = spy(DiscardPile.class);
        DiscardPile spyDiscardPile3 = spy(DiscardPile.class);
        when(spyDiscardPile1.value()).thenReturn(2);
        when(spyDiscardPile2.value()).thenReturn(5);
        when(spyDiscardPile3.value()).thenReturn(5);

        // Create input
        Player p1 = new Player("P1", spyHand, spyDiscardPile1);
        Player p2 = new Player("P2", spyHand, spyDiscardPile2);
        Player p3 = new Player("P3", spyHand, spyDiscardPile3);
        List<Player> winnersAfterCmpHands = new ArrayList<>(Arrays.asList(p1, p2, p3));
        PlayerList playerList = new PlayerList();

        // Verify results
        List<Player> winners = playerList.compareUsedPiles(winnersAfterCmpHands);
        assertEquals(winners.size(), 2);
        assertEquals(winners.get(0), p2);
        assertEquals(winners.get(1), p3);
    }

    /**
     * Test for no players has the equally highest discard pile value (empty input)
     */
    @Test
    public void testCompareUsedPilesNoWinner(){
        // Create input
        List<Player> winnersAfterCmpHands = new ArrayList<>();
        PlayerList playerList = new PlayerList();

        // Verify results
        List<Player> winners = playerList.compareUsedPiles(winnersAfterCmpHands);
        assertEquals(winners.size(), 0);
    }

    /**
     * Test when valid winner is inputted to the setBeginner()
     */
    @Test
    public void testSetBeginnerWithWinner(){
        // Create input
        LinkedList<Player> players = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            players.add(new Player("P"+i));
        }
        Player p10 = new Player("P10");
        players.add(p10);
        PlayerList playerList = new PlayerList(players);

        // Verify results
        assertEquals(playerList.currentPlayer, 0);
        playerList.setBeginner(p10);
        assertEquals(playerList.currentPlayer, 10);
    }

    /**
     * Test when null is inputted to the setBeginner()
     */
    @Test
    public void testSetBeginnerWithNullInput(){
        // Create input
        PlayerList playerList = new PlayerList();

        // Verify results
        assertEquals(playerList.currentPlayer, 0);
        playerList.setBeginner(null);
        assertEquals(playerList.currentPlayer, 0);
    }

    /**
     * Test for getting the number of players available to choose with input
     */
    @Test
    public void testNumAvailablePlayersHasInput(){
        // Create test double and define its behavior
        Player mockPlayer1 = mock(Player.class);
        Player mockPlayer2 = mock(Player.class);
        Player mockPlayer3 = mock(Player.class);
        when(mockPlayer1.isAlive()).thenReturn(true);
        when(mockPlayer2.isAlive()).thenReturn(true);
        when(mockPlayer3.isAlive()).thenReturn(true);
        when(mockPlayer1.isProtected()).thenReturn(false);
        when(mockPlayer2.isProtected()).thenReturn(false);
        when(mockPlayer3.isProtected()).thenReturn(false);

        // Create input
        LinkedList<Player> players = new LinkedList<>(Arrays.asList(mockPlayer1, mockPlayer2, mockPlayer3));
        PlayerList playerList = new PlayerList(players);

        // Verify results
        int num = playerList.getNumAvailablePlayers(mockPlayer1);
        assertEquals(num, 2);
    }

    /**
     * Test for getting the number of players available to choose with null input
     */
    @Test
    public void testNumAvailablePlayersNullInput(){
        // Create test double and define its behavior
        Player mockPlayer1 = mock(Player.class);
        Player mockPlayer2 = mock(Player.class);
        Player mockPlayer3 = mock(Player.class);
        when(mockPlayer1.isAlive()).thenReturn(true);
        when(mockPlayer2.isAlive()).thenReturn(true);
        when(mockPlayer3.isAlive()).thenReturn(true);
        when(mockPlayer1.isProtected()).thenReturn(false);
        when(mockPlayer2.isProtected()).thenReturn(false);
        when(mockPlayer3.isProtected()).thenReturn(false);

        // Create input
        LinkedList<Player> players = new LinkedList<>(Arrays.asList(mockPlayer1, mockPlayer2, mockPlayer3));
        PlayerList playerList = new PlayerList(players);

        // Verify results
        int num = playerList.getNumAvailablePlayers(null);
        assertEquals(num, 3);
    }

    /**
     * Test for getting the number of players available to choose 
     * where some players in the player list are unable to choose
     */
    @Test
    public void testNumAvailablePlayersDeadProtected(){
        // Create test double and define its behavior
        Player mockPlayer1 = mock(Player.class);
        Player mockPlayer2 = mock(Player.class);
        Player mockPlayer3 = mock(Player.class);
        when(mockPlayer1.isAlive()).thenReturn(false);
        when(mockPlayer2.isAlive()).thenReturn(true);
        when(mockPlayer3.isAlive()).thenReturn(true);
        when(mockPlayer1.isProtected()).thenReturn(false);
        when(mockPlayer2.isProtected()).thenReturn(true);
        when(mockPlayer3.isProtected()).thenReturn(false);

        // Create input
        LinkedList<Player> players = new LinkedList<>(Arrays.asList(mockPlayer1, mockPlayer2, mockPlayer3));
        PlayerList playerList = new PlayerList(players);

        // Verify results
        int num = playerList.getNumAvailablePlayers(null);
        assertEquals(num, 1);
    }

    /**
     * Test when there's player with Jester Token in the winners list
     */
    @Test
    public void testCheckWinnerForJesterTokenWithJester(){
        // Create input
        Player p1 = new Player("P1");
        Player p2 = new Player("P2");
        Player p3 = new Player("P3");
        LinkedList<Player> players = new LinkedList<>(Arrays.asList(p1, p2));
        PlayerList playerList = new PlayerList(players);
        playerList.setJesterToken(p1, p3);      // set Jester token for p3
        LinkedList<Player> winners = new LinkedList<>(Arrays.asList(p1, p2, p3));

        // Verify results
        Player output = playerList.checkWinnerForJesterToken(winners);
        assertEquals(output, p1);
    }

    /**
     * Test when there's no player with Jester Token in the winners list
     */
    @Test
    public void testCheckWinnerForJesterTokenNoJester(){
        // Create input
        Player p1 = new Player("P1");
        Player p2 = new Player("P2");
        Player p3 = new Player("P3");
        LinkedList<Player> players = new LinkedList<>(Arrays.asList(p1, p2));
        PlayerList playerList = new PlayerList(players);
        playerList.setJesterToken(p1, p3);      // set Jester token for p3
        LinkedList<Player> winners = new LinkedList<>(Arrays.asList(p1, p2));

        // Verify results
        Player output = playerList.checkWinnerForJesterToken(winners);
        assertEquals(output, null);
    }

    private Player createMockPlayerWithCard(String cardName, int cardValue) {
        // 创建 Card 对象的 mock
        Card mockCard = Mockito.mock(Card.class);
        when(mockCard.name()).thenReturn(cardName);
        when(mockCard.value()).thenReturn(cardValue);

        // 创建 Hand 对象的 mock
        Hand mockHand = Mockito.mock(Hand.class);
        when(mockHand.peek(0)).thenReturn(mockCard);

        // 创建 Player 对象的 mock
        Player mockPlayer = Mockito.mock(Player.class);
        when(mockPlayer.getHand()).thenReturn(mockHand);

        return mockPlayer;
    }

    /**
     * The case that a single winner (no counts in discards) is found
     */
    @Test
    public void testCompareHandSingleWinner() {
        Hand mockHand1 = Mockito.mock(Hand.class);
        Hand mockHand2 = Mockito.mock(Hand.class);
        when(mockHand1.peek(0)).thenReturn(Card.PRINCESS);
        when(mockHand2.peek(0)).thenReturn(Card.COUNTESS);

        DiscardPile mockDiscardPile1 = Mockito.mock(DiscardPile.class);
        DiscardPile mockDiscardPile2 = Mockito.mock(DiscardPile.class);
        when(mockDiscardPile1.getCards()).thenReturn(new ArrayList<Card>(Arrays.asList(Card.GUARD)));
        when(mockDiscardPile2.getCards()).thenReturn(new ArrayList<Card>(Arrays.asList(Card.BARON)));

        Player player1 = new Player("u1", mockHand1, mockDiscardPile1);
        Player player2 = new Player("u2", mockHand2, mockDiscardPile2);

        PlayerList playerList = new PlayerList();
        List<Player> winners = playerList.compareHand(Arrays.asList(player1, player2));

        assertEquals(winners, Arrays.asList(player1));
    }

    /**
     * The case that multiple winners (no counts in discards) are found
     */
    @Test
    public void testCompareHandMultipleWinner() {
        Hand mockHand1 = Mockito.mock(Hand.class);
        Hand mockHand2 = Mockito.mock(Hand.class);
        when(mockHand1.peek(0)).thenReturn(Card.PRINCE);
        when(mockHand2.peek(0)).thenReturn(Card.PRINCE);

        DiscardPile mockDiscardPile1 = Mockito.mock(DiscardPile.class);
        DiscardPile mockDiscardPile2 = Mockito.mock(DiscardPile.class);
        when(mockDiscardPile1.getCards()).thenReturn(new ArrayList<Card>(Arrays.asList(Card.GUARD)));
        when(mockDiscardPile2.getCards()).thenReturn(new ArrayList<Card>(Arrays.asList(Card.BARON)));

        Player player1 = new Player("u1", mockHand1, mockDiscardPile1);
        Player player2 = new Player("u2", mockHand2, mockDiscardPile2);

        PlayerList playerList = new PlayerList();
        List<Player> winners = playerList.compareHand(Arrays.asList(player1, player2));

        assertEquals(winners, Arrays.asList(player1, player2));
    }

    /**
     * The case that Princess beats Bishop (no counts in discards)
     */
    @Test
    public void testCompareHandBishopPrincess() {
        Hand mockHand1 = Mockito.mock(Hand.class);
        Hand mockHand2 = Mockito.mock(Hand.class);
        when(mockHand1.peek(0)).thenReturn(Card.PRINCESS);
        when(mockHand2.peek(0)).thenReturn(Card.BISHOP);

        DiscardPile mockDiscardPile1 = Mockito.mock(DiscardPile.class);
        DiscardPile mockDiscardPile2 = Mockito.mock(DiscardPile.class);
        when(mockDiscardPile1.getCards()).thenReturn(new ArrayList<Card>(Arrays.asList(Card.GUARD)));
        when(mockDiscardPile2.getCards()).thenReturn(new ArrayList<Card>(Arrays.asList(Card.BARON)));

        Player player1 = new Player("u1", mockHand1, mockDiscardPile1);
        Player player2 = new Player("u2", mockHand2, mockDiscardPile2);

        PlayerList playerList = new PlayerList();
        List<Player> winners = playerList.compareHand(Arrays.asList(player1, player2));

        assertEquals(winners, Arrays.asList(player1)); 
    }

    /**
     * The case that Countess + 2 Counts beat Princess
     */
    @Test
    public void testCompareHand2Counts() {
        Hand mockHand1 = Mockito.mock(Hand.class);
        Hand mockHand2 = Mockito.mock(Hand.class);
        when(mockHand1.peek(0)).thenReturn(Card.PRINCESS);
        when(mockHand2.peek(0)).thenReturn(Card.COUNTESS);

        DiscardPile mockDiscardPile1 = Mockito.mock(DiscardPile.class);
        DiscardPile mockDiscardPile2 = Mockito.mock(DiscardPile.class);
        when(mockDiscardPile1.getCards()).thenReturn(new ArrayList<Card>(Arrays.asList(Card.BARON)));
        when(mockDiscardPile2.getCards()).thenReturn(new ArrayList<Card>(Arrays.asList(Card.COUNT, Card.COUNT)));

        Player player1 = new Player("u1", mockHand1, mockDiscardPile1);
        Player player2 = new Player("u2", mockHand2, mockDiscardPile2);

        PlayerList playerList = new PlayerList();
        List<Player> winners = playerList.compareHand(Arrays.asList(player1, player2));

        assertEquals(winners, Arrays.asList(player2)); 
    }
}
