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
                    System.out.printf("There must be at least %d players, please add more players!", minNum);
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
                System.out.printf("There are already %d players in the game, game should start now!", maxNum);
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
        String cardPosition;
        while (true) {
            System.out.print("Which card would you like to play (0 for first, 1 for second): ");
            cardPosition = in.nextLine();
            if (!cardPosition.equals("0") && !cardPosition.equals("1")) {
                System.out.println("Please enter a valid card position");
                continue;
            } else {
                return Integer.parseInt(cardPosition);
            }
        }       
    }

    /**
     * Choose opponent for cards requiring an opponent
     * 
     * @param playerList
     *          The entire player list
     * @param user
     *          If doesn't allow the user to choose himself, set as the user who is choosing the opponent
     *          Otherwise set as null
     * @param alreadyChosen
     *          If some player has already been chosen, set `chosen` as that player
     *          Otherwise set as null
     * @return The player chosen as opponent
     */
    public Player getOpponent(PlayerList playerList, Player user, Player alreadyChosen) {
        Player opponent = null;

        while (true) {
            System.out.print("Who would you like to target: ");
            String opponentName = in.nextLine();
            opponent = playerList.getPlayer(opponentName);

            if (opponent == null) {
                System.out.println("This player is not in the game");

            } else if (opponent.isProtected()) {
                System.out.println("This player is protected by a handmaiden");

            } else if (!opponent.isAlive()) {
                System.out.println("This player is out of cards");

            } else if (user != null && opponent.getName().equals(user.getName())) {
                System.out.println("You cannot target yourself");

            } else if (alreadyChosen != null && opponent.getName().equals(alreadyChosen.getName())) {
                System.out.println("You already chose that player");

            } else {
                break;
            }
        }

        return opponent;
    }

    /**
     * Pick the card players want to guess when they use the "Guard" card
     * @return The card the player wants to guess
     */
    public String pickCardWhenGuard() {
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

    /**
     * Let player choose the player whose hand they want to look at
     * @return The index of the choosen player
     */
    public int choosePlayerWhenCardinal() {
        System.out.println();
        System.out.println("Please choose the player would you like to look at");
        System.out.printf("0 for first, 1 for second, press enter if you don't want to look at either of their hands: ");

        String playerPos;
        while (true) {
            playerPos = in.nextLine();
            if (playerPos == "") {
                return -1;
            }
            if (!playerPos.equals("0") && !playerPos.equals("1")) {
                System.out.println("Please enter a valid position");
                continue;
            } else {
                return Integer.parseInt(playerPos);
            }
        }       
    }    

    /**
     * Pick the card number players want to guess when they use the "Bishop" card
     * @return The card number the player wants to guess
     */
    public int pickCardNumberWhenBishop() {
        int cardNumber;

        while (true) {
            System.out.print("Which card number would you like to guess, from 0-9: ");
            cardNumber = in.nextInt();
            in.nextLine();
            if (cardNumber < 0 || cardNumber > 9) {
                System.out.println("Invalid card number");
                continue;
            } else {
                break;
            }
        }
        return cardNumber;
    }

    /**
     * Let the opponent of Bishop decides if he wants to discard the revealed card and draw a new card
     * @return The choice of the player, 0 for draw a new card, 1 for do nothing
     */
    public int drawWhenBishop() {
        System.out.println("0. Yes");
        System.out.println("1. No");
        System.out.print("Your choice: ");
        return in.nextInt();
    }

    /**
     * Choose the number of player's hands the user want to inspect for Baroness
     * @return The number of opponents need to be chosen
     */
    public int getNumOpponents() {
        int numOpponents;
        
        while (true) {
            System.out.print("How many player's hands do you want to inspect (1 or 2): ");
            numOpponents = in.nextInt();
            in.nextLine();
            if (numOpponents != 1 && numOpponents != 2) {
                System.out.println("Invalid number");
                continue;
            } else {
                break;
            }
        }
        return numOpponents;   
    }
}
