package edu.cmu.f23qa.loveletter;

import java.util.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class PlayerListTest {
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
        Player spyPlayer1 = mock(Player.class);
        Player spyPlayer2 = mock(Player.class);
        Player spyPlayer3 = mock(Player.class);
        when(spyPlayer1.isAlive()).thenReturn(true);
        when(spyPlayer2.isAlive()).thenReturn(true);
        when(spyPlayer3.isAlive()).thenReturn(true);
        when(spyPlayer1.isProtected()).thenReturn(false);
        when(spyPlayer2.isProtected()).thenReturn(false);
        when(spyPlayer3.isProtected()).thenReturn(false);

        // Create input
        LinkedList<Player> players = new LinkedList<>(Arrays.asList(spyPlayer1, spyPlayer2, spyPlayer3));
        PlayerList playerList = new PlayerList(players);

        // Verify results
        int num = playerList.getNumAvailablePlayers(spyPlayer1);
        assertEquals(num, 2);
    }

    /**
     * Test for getting the number of players available to choose with null input
     */
    @Test
    public void testNumAvailablePlayersNullInput(){
        // Create test double and define its behavior
        Player spyPlayer1 = mock(Player.class);
        Player spyPlayer2 = mock(Player.class);
        Player spyPlayer3 = mock(Player.class);
        when(spyPlayer1.isAlive()).thenReturn(true);
        when(spyPlayer2.isAlive()).thenReturn(true);
        when(spyPlayer3.isAlive()).thenReturn(true);
        when(spyPlayer1.isProtected()).thenReturn(false);
        when(spyPlayer2.isProtected()).thenReturn(false);
        when(spyPlayer3.isProtected()).thenReturn(false);

        // Create input
        LinkedList<Player> players = new LinkedList<>(Arrays.asList(spyPlayer1, spyPlayer2, spyPlayer3));
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
        Player spyPlayer1 = mock(Player.class);
        Player spyPlayer2 = mock(Player.class);
        Player spyPlayer3 = mock(Player.class);
        when(spyPlayer1.isAlive()).thenReturn(false);
        when(spyPlayer2.isAlive()).thenReturn(true);
        when(spyPlayer3.isAlive()).thenReturn(true);
        when(spyPlayer1.isProtected()).thenReturn(false);
        when(spyPlayer2.isProtected()).thenReturn(true);
        when(spyPlayer3.isProtected()).thenReturn(false);

        // Create input
        LinkedList<Player> players = new LinkedList<>(Arrays.asList(spyPlayer1, spyPlayer2, spyPlayer3));
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
}