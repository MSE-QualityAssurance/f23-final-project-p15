package edu.cmu.f23qa.loveletter;

import java.util.*;

public class PlayerList {

    private LinkedList<Player> players;
    private int currentPlayer = 0;   // the index of the player in the player list
    private int tokensToWin = 0;
    private Player playerSetJesterToken = null;
    private Player playerWithJesterToken = null;
    private Player playerWithConstable = null;

    public PlayerList() {
        this.players = new LinkedList<>();
    }

    /**
     * Set the number of tokens to win the game
     */
    public void setTokensToWin() {
        Map<Integer, Integer> playerNumToToken = new HashMap<>();
        playerNumToToken.put(2, 7);
        playerNumToToken.put(3, 5);
        playerNumToToken.put(4, 4);
        playerNumToToken.put(5, 4);
        playerNumToToken.put(6, 4);
        playerNumToToken.put(7, 4);
        playerNumToToken.put(8, 4);
        
        int numPlayers = this.players.size();

        this.tokensToWin = playerNumToToken.getOrDefault(numPlayers, 0);
    }

    /**
     * Add a list of players to the PlayerList.
     *
     * @param playerNames
     *          a list of player names
     */
    public void addPlayers(List<String> playerNames) {
        for (String name : playerNames) {
            players.addLast(new Player(name));
        }
    }

    /**
     * Gets the first player in the list and adds them to end of the list.
     *
     * @return the first player in the list
     */
    public Player getCurrentPlayer() {
        int pre = currentPlayer;
        currentPlayer = (currentPlayer + 1) % players.size();
        return players.get(pre);
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
     * Returns all the players alive in this round.
     *
     * @return a list of alive players in this round
     */
    public List<Player> getAlivePlayers() {
        List<Player> winners = new ArrayList<>();
        for (Player p : players) {
            if (p.isAlive()) {
                winners.add(p);
            }
        }
        return winners;
    }

    /**
     * Returns the list of players who reach the target number of token of the game.
     *
     * @return the list of game winner
     */
    public List<Player> getGameWinner() {
        List<Player> winners = new ArrayList<>();
        int maxTokens = 0;

        for (Player p : players) {
            if (p.getTokens() >= tokensToWin && p.getTokens() > maxTokens) {
                winners.clear();
                winners.add(p);
                maxTokens = p.getTokens();
            }
            else if (p.getTokens() >= tokensToWin && p.getTokens() == maxTokens) {
                winners.add(p);
            }
        }

        return winners;
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
     * Returns the players with the highest hand value.
     * 
     * @return the players with the highest hand value
     */
    public List<Player> compareHand(List<Player> alivePlayers) {
        List<Player> winners = new ArrayList<>();
        int maxHand = -1;
        for (Player player : alivePlayers) {
            int hand = player.getHand().peek(0).value();
            String handName = player.getHand().peek(0).name();
            // Bishop can't beat Princess
            if (handName.equalsIgnoreCase("Bishop") && maxHand == 8) {
                continue;
            }
            if (hand > maxHand) {
                maxHand = hand;
                winners.clear();
                winners.add(player);
            } else if (hand == maxHand) {
                winners.add(player);
            }
        }
        return winners;
    }

    /**
     * Returns the player with the highest used pile value.
     *
     * @return the player with the highest used pile value; 
     *         or null if there is a tie
     */
    public List<Player> compareUsedPiles(List<Player> winnersAfterCmpHands) {
        List<Player> winners = new ArrayList<>();
        int maxDiscardedSum = -1;
        for (Player p : winnersAfterCmpHands) {
            int discardedSum = p.getDiscarded().value();
            if (discardedSum > maxDiscardedSum) {
                maxDiscardedSum = discardedSum;
                winners.clear();
                winners.add(p);
            } else if (discardedSum == maxDiscardedSum) {
                winners.add(p);
            } 
        }

        return winners;
    }



    /**
     * Returns the number of players in the list.
     * @return the number of players in the list
     */
    public int getNumPlayers() {
        return players.size();
    }


    /**
     * Reset the beginning player at the beginning of a round
     * 
     * @param winner
     *      the winner of last round
     * @return
     */
    public void setBeginner(Player winner) {
        if (winner != null) { // there is a winner from last round, starts with him
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i) == winner) {
                    this.currentPlayer = i;
                    break;
                }
            }
            return;
        }
        else
            return;
    }

    /**
     * Return the number of players available for choosing
     * 
     * @param exceptPlayer
     *          extra player who is not allowed to be targeted, e.g. the player who is going to choose opponent
     *          null if no such requirement
     * @return the number of available players
     */
    public int getNumAvailablePlayers(Player exceptPlayer) {
        int num = 0;
        for (Player p : players) {
            if (p.isAlive() && !p.isProtected() && p != exceptPlayer) {
                num++;
            }
        }
        return num;
    }

    /**
     * Set the Jester token for the given target
     * 
     * @param player 
     *          the player who sets the Jester token
     * @param target
     *          the player who will get the Jester token
     */
    public void setJesterToken(Player player, Player target) {
        this.playerSetJesterToken = player;
        this.playerWithJesterToken = target;
    }

    /**
     * Check whether the winner holds the Jester token
     * 
     * @param winners
     *          a list of winners
     * @return the player who set the Jester token, if the player who holds the Jester token is a winner
     */
    public Player checkWinnerForJesterToken(List<Player> winners) {
        for (Player winner : winners) {
            if (winner == playerWithJesterToken) {
                return playerSetJesterToken;
            }
        }

        return null;
    }

    /**
     * Set the playerWithConstable as who discarded the card
     * @param player
     *          he player who discarded the card
     */
    public void setPlayerWithConstable(Player player) {
        this.playerWithConstable = player;
    }

    /**
     * Return the player who discarded the card
     * @return the player who discarded the card
     */
    public Player checkPlayerForConstableToken() {
        return playerWithConstable;
    }
}