package edu.cmu.f23qa.loveletter;

import java.util.Scanner;

/**
 * The main game class. Contains methods for running the game.
 */
public class Game extends GameActions {
    private PlayerList players;
    private Deck deck;

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
        this.deck = new Deck();
        this.in = in;
    }

    /**
     * Sets up the players that make up the player list.
     */
    public void setPlayers() {
        System.out.print("Enter player name (empty when done): ");
        String name = in.nextLine();

        while (!name.isEmpty()) {
            if (!this.players.addPlayer(name)) {
                System.out.println("Player is already in the game");
            }
            System.out.print("Enter player name (empty when done): ");
            name = in.nextLine();
        }
    }

    /**
     * The main game loop.
     */
    public void start() {
        while (players.getGameWinner() == null) {
            players.reset();
            setDeck();
            players.dealCards(deck);
            while (!players.checkForRoundWinner() && deck.hasMoreCards()) {
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
            }

            Player winner;
            if (players.checkForRoundWinner() && players.getRoundWinner() != null) {
                winner = players.getRoundWinner();
            } else {
                winner = players.compareUsedPiles();
                winner.addToken();
            }
            winner.addToken();
            System.out.println(winner.getName() + " has won this round!");
            players.print();
        }
        Player gameWinner = players.getGameWinner();
        System.out.println(gameWinner + " has won the game and the heart of the princess!");

    }

    /**
     * Builds a new full deck and shuffles it.
     */
    private void setDeck() {
        this.deck.build();
        this.deck.shuffle();
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
}
