package edu.cmu.f23qa.loveletter;

import java.util.*;

public class Reader {
    private Scanner in;

    public Reader(Scanner in) {
        this.in = in;
    }    

    /**
     * Get players from input.
     * @param minNum 
     *          The minimum number of players required for the game
     * @param maxNum
     *          The maximum number of players allowed for the game
     * @return A list of player names
     */
    public List<String> getPlayers(int minNum, int maxNum) {
        List<String> playerNames = new ArrayList<>();

        String name;
        while (true) {
            System.out.print("Enter player name (empty when done): ");
            name = in.nextLine();

            // Check the number of players when user wants to end input
            if (name.isEmpty()) {
                if (playerNames.size() >= minNum) {
                    System.out.println("Players setup already, game should start now!");
                    return playerNames;
                } else if (playerNames.size() < minNum) {
                    System.out.println("There must be at least 2 players, please add more players!");
                    continue;
                }
            }

            // Check duplicated names
            if (playerNames.contains(name)) {
                System.out.println("Player is already in the game");
                continue;
            }

            playerNames.add(name);

            // Start the game immediately when the number of players reach maximum
            if (playerNames.size() == maxNum) {
                System.out.println("There are already 4 players in the game, game should start now!");
                return playerNames;
            }
        }
    }

    /**
     * Let player choose the card they want to play from their hand
     * @return The index of the card the player want to play in his hand
     */
    public int getCard() {
        System.out.println();
        System.out.print("Which card would you like to play (0 for first, 1 for second): ");
        String cardPosition = in.nextLine();
        while (!cardPosition.equals("0") && !cardPosition.equals("1")) {
            System.out.println("Please enter a valid card position");
            System.out.print("Which card would you like to play (0 for first, 1 for second): ");
            cardPosition = in.nextLine();
        }

        return Integer.parseInt(cardPosition);
    }

    /**
     * Choose opponent for cards requiring an opponent, allowing to target user itself
     * @param playerList
     *          The entire player list
     * @return The player chosen as opponent
     */
    Player getOpponent(PlayerList playerList) {
        Player opponent = null;

        while (true) {
            System.out.print("Who would you like to target: ");
            String opponentName = in.nextLine();
            opponent = playerList.getPlayer(opponentName);

            if (opponent == null) {
                System.out.println("This player is not in the game");

            } else if (opponent.isProtected()) {
                System.out.println("This player is protected by a handmaiden");

            } else if (!opponent.getHand().hasCards()) {
                System.out.println("This player is out of cards");

            } else {
                break;
            }
        }

        return opponent;
    }

    /**
     * Choose opponent for cards requiring an opponent, disallowing to target player itself
     * @param playerList
     *          The entire player list
     * @param user
     *          The player who is choosing the opponent
     * @return The player chosen as opponent
     */
    Player getOpponentNotSelf(PlayerList playerList, Player user) {
        Player opponent = getOpponent(playerList);

        while (opponent.getName().equals(user.getName())) {
            System.out.println("You cannot target yourself");
            opponent = getOpponent(playerList);
        }

        return opponent;
    }

    /**
     * Pick the card players want to guess when they use the "Guard" card
     * @return The card the player wants to guess
     */
    String pickCardWhenGuard() {
        ArrayList<String> cardNames = new ArrayList<>(Arrays.asList(Card.CARD_NAMES));

        String cardName;
        while (true) {
            System.out.print("Which card would you like to guess: ");
            cardName = in.nextLine();

            if (!cardNames.contains(cardName.toLowerCase()) || cardName.equalsIgnoreCase("guard")) {
                System.out.println("Invalid card name");
                continue;
            } else {
                return cardName;
            }
        }
    }
}
