package edu.cmu.f23qa.loveletter;

import java.util.LinkedList;
import java.util.Stack;

public class PlayerList {

    private LinkedList<Player> players;

    public PlayerList() {
        this.players = new LinkedList<>();
    }

    /**
     * Adds a new Player object with the given name to the PlayerList.
     *
     * @param name
     *          the given player name
     *
     * @return true if the player is not already in the list and can be added, false if not
     */
    public boolean addPlayer(String name) {
        for (Player p : players) {
            if (p.getName().equalsIgnoreCase(name)) {
                return false;
            }
        }
        players.addLast(new Player(name));
        return true;
    }

    /**
     * Gets the first player in the list and adds them to end of the list.
     *
     * @return the first player in the list
     */
    public Player getCurrentPlayer() {
        Player current = players.removeFirst();
        players.addLast(current);
        return current;
    }

    /**
     * Resets all players within the list.
     */
    public void reset() {
        for (Player p : players) {
            p.getHand().clear();
            p.getDiscarded().clear();
        }
    }

    /**
     * Prints the used pile of each Player in the list.
     */
    public void printUsedPiles() {
        for (Player p : players) {
            System.out.println("\n" + p.getName());
            p.getDiscarded().print();
        }
    }

    /**
     * Prints each Player in the list.
     */
    public void print() {
        System.out.println();
        for (Player p : players) {
            System.out.println(p);
        }
        System.out.println();
    }

    /**
     * Checks the list for a single Player with remaining cards.
     *
     * @return true if there is a winner, false if not
     */
    public boolean checkForRoundWinner() {
        int count = 0;
        for (Player p : players) {
            if (p.getHand().hasCards()) {
                count++;
            }
        }
        return count == 1;
    }

    /**
     * Returns the winner of the round.
     *
     * @return the round winner
     */
    public Stack<Player> getRoundWinner() {
        Stack<Player> winners = new Stack<>();
        for (Player p : players) {
            if (p.getHand().hasCards()) {
                winners.push(p);
                return winners;
            }
        }
        return null;
    }

    /**
     * Returns the winner of the game.
     *
     * @return the game winner
     */
    public Player getGameWinner() {
        for (Player p : players) {
            if (p.getTokens() == 5) {
                return p;
            }
        }
        return null;
    }

    /**
     * Deals a card to each Player in the list.
     *
     * @param deck
     *          the deck of cards
     */
    public void dealCards(Deck deck) {
        for (Player p : players) {
            p.getHand().add(deck.draw());
        }
    }

    /**
     * Gets the player with the given name.
     *
     * @param name
     *          the name of the desired player
     *
     * @return the player with the given name or null if there is no such player
     */
    public Player getPlayer(String name) {
        for (Player p : players) {
            if (p.getName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Returns the player with the highest used pile value.
     *
     * @return the player with the highest used pile value; 
     *         or null if there is a tie
     */
    public Stack<Player> compareUsedPiles() {
        Player winner = players.getFirst();
        Stack<Player> winners = new Stack<>();
        for (Player p : players) {
            if (p.getDiscarded().value() > winner.getDiscarded().value()) {
                // add p to the winners
                winner = p;
            }
            else if (p.getDiscarded().value() == winner.getDiscarded().value()) {
                // there's a tie, see if it's a final round
                // if it is, the game should go on with an extra round
                if (p.getTokens() == winner.getTokens() && ifFinalRound(p))
                    return null;
                // if not, accept multiple winners
                else 
                    winners.push(p);
            }
        }
        winners.push(winner);
        return winners;
    }

    private boolean ifFinalRound(Player p) {
        int numPlayers = this.getNumPlayers();
        if (numPlayers == 2) {
            return p.getTokens()+1 == 7;
        }
        else if (numPlayers == 3) {
            return p.getTokens()+1 == 5;
        }
        else if (numPlayers == 4) {
            return p.getTokens()+1 == 4;
        }
        return false;
    }

    /**
     * Returns the number of players in the list.
     * @return the number of players in the list
     */
    public int getNumPlayers() {
        return players.size();
    }

}
