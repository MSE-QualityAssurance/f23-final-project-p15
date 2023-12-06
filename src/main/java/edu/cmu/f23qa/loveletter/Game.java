package edu.cmu.f23qa.loveletter;

import java.util.*;

/**
 * The main game class. Contains methods for running the game.
 */
public class Game extends GameActions {
    private PlayerList players;
    private Deck deck;
    private Player lastRoundWinner = null;

    /**
     * The input stream.
     */
    private Scanner in;

    /**
     * Public constructor for a Game object.
     * @param in
     *          the input stream
     */
    public Game(Scanner in) {
        this.players = new PlayerList();
        // store the number of players

        this.deck = new Deck();
        this.in = in;
    }

    /**
     * Sets up the players that make up the player list.
     * Refactored already for 2-4 players by Huanmi on Nov 30.
     * To be updated for 5-8 players in the future.
     */
    public void setPlayers() {
        String name;
        while (true) {
            System.out.print("Enter player name (empty when done): ");
            name = in.nextLine();

            // check if we can progress with empty input
            if (name.isEmpty() && this.players.getNumPlayers() >= 2) {
                System.out.println("Players setup already, game should start now!");
                return;
            }
            else if (name.isEmpty() && this.players.getNumPlayers() < 2) {
                System.out.println("There must be at least 2 players, please add more players!");
                continue;
            }

            // valid input
            if (!this.players.addPlayer(name)) {
                System.out.println("Player is already in the game");
                continue;
            }
            if (this.players.getNumPlayers() == 4) {
                System.out.println("There are already 4 players in the game, game should start now!");
                return;
            }
        }

    }

    /**
     * The main game loop.
     */
    public void start() {
        while (players.getGameWinner().size() != 1) {
            players.reset(); // clear their hands and discards
            setDeck();

            players.dealCards(deck);
            players.setBeginner(lastRoundWinner);
            
            while (players.getAlivePlayers().size() != 1 && deck.hasMoreCards()) {
                Player turn = players.getCurrentPlayer();

                if (turn.getHand().hasCards()) {
                    players.printUsedPiles();
                    System.out.println("\n" + turn.getName() + "'s turn:");
                    if (turn.isProtected()) {
                        turn.switchProtection();
                    }
                    turn.getHand().add(deck.draw());

                    int royaltyPos = turn.getHand().royaltyPos();
                    if (royaltyPos != -1) {
                        if (royaltyPos == 0 && turn.getHand().peek(1).value() == 7) {
                            playCard(turn.getHand().remove(1), turn);
                        } else if (royaltyPos == 1 && turn.getHand().peek(0).value() == 7) {
                            playCard(turn.getHand().remove(0), turn);
                        } else {
                            playCard(getCard(turn), turn);
                        }
                    } else {
                        playCard(getCard(turn), turn);
                    }
                }
                // check if the round ends
                checkForRoundWinner();  
            }
            
            // check if the game ends
            List<Player> gameWinners = players.getGameWinner();
            if (gameWinners.size() == 1) {
                System.out.println(gameWinners.get(0) + " has won the game and the heart of the princess!");
                break;
            }
            // in case of a tie
            else if (gameWinners.size() > 1) {
                System.out.println("It's a tie! Let's play one more round!");
            }

        } // end of game

    }

    /**
     * Builds a new full deck and shuffles it.
     */
    private void setDeck() {
        this.deck.build();
        this.deck.shuffle();
        // remove cards from the deck initially, according to the rule
        this.deck.removeCards(this.players.getNumPlayers());
    }

    /**
     * Determines the card used by the player and performs the card's action.
     * @param card
     *          the played card
     * @param user
     *          the player of the card
     */
    private void playCard(Card card, Player user) {
        int value = card.value();
        user.getDiscarded().add(card);
        if (value < 4 || value == 5 || value == 6) {
            Player opponent = getOpponent(in, players, user);
            if (value == 1) {
                useGuard(in, opponent);
            } else if (value == 2) {
                usePriest(opponent);
            } else if (value == 3) {
                useBaron(user, opponent);
            } else if (value == 5) {
                usePrince(opponent, deck);
            } else if (value == 6) {
                useKing(user, opponent);
            }
        } else {
            if (value == 4) {
                useHandmaiden(user);
            } else if (value == 8) {
                usePrincess(user);
            }
        }
    }

    /**
     * Allows for the user to pick a card from their hand to play.
     *
     * @param user
     *      the current player
     *
     * @return the chosen card
     */
    private Card getCard(Player user) {
        user.getHand().print();
        System.out.println();
        System.out.print("Which card would you like to play (0 for first, 1 for second): ");
        String cardPosition = in.nextLine();
        while (!cardPosition.equals("0") && !cardPosition.equals("1")) {
            System.out.println("Please enter a valid card position");
            System.out.print("Which card would you like to play (0 for first, 1 for second): ");
            cardPosition = in.nextLine();
        }

        int idx = Integer.parseInt(cardPosition);
        return user.getHand().remove(idx);
    }

    /** 
     * Check for winner after a round ends
     * @param opponent
     * the current opponent
     */
    private List<Player> checkForRoundWinner() {
        List<Player> winners = new ArrayList<Player>();
        List<Player> alivePlayers = players.getAlivePlayers();
        // only one player alive, this round ends and he becomes the winner
        if (alivePlayers.size() == 1) {
            winners = alivePlayers;
        }
        // multiple players alive, check their hands
        else {
            List<Player> winnersAfterCmpHands = players.compareHand(alivePlayers);
            if (winnersAfterCmpHands.size() == 1) {
                winners = winnersAfterCmpHands;
            } else {
                winners = players.compareUsedPiles(winnersAfterCmpHands); 
            }
        }

        for (Player winner : winners) {
            winner.addToken();
        }

        lastRoundWinner = winners.get(0);

        return winners;
    }
}
