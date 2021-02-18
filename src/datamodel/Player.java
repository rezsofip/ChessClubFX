package datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Random;

public class Player {

    private String name;
    private int eloPoint;
    private int gamesPlayed;
    private int gamesWon;
    private int gamesLost;
    private int gamesDraw;
    private double totalPoints;
    private ObservableList<Player> opponentList;
    private Random rand = new Random();
    private String playerLevel;

    public ObservableList<Player> getOpponentList() {
        return opponentList;
    }


    public Player(String name) {
        this.name = name;
        this.eloPoint = 1000;
        this.gamesPlayed = 0;
        this.gamesWon = 0;
        this.gamesDraw = 0;
        this.gamesLost = 0;
        this.opponentList = FXCollections.observableArrayList();
        this.totalPoints = 0;
        this.playerLevel= "Kezdő";
    }

    public double getTotalPoints() {
        return this.totalPoints;
    }

    public void setTotalPoints() {
        this.totalPoints = gamesWon + gamesDraw * 0.5;
    }

    public String getPlayerLevel() {
        return playerLevel;
    }

    public Player(String name, int eloPoint, String playerLevel) {
        this.name = name;
        this.eloPoint = eloPoint;
        this.gamesPlayed = 0;
        this.gamesWon = 0;
        this.gamesDraw = 0;
        this.gamesLost = 0;
        this.opponentList = FXCollections.observableArrayList();
        this.totalPoints = 0;
        this.playerLevel= playerLevel;

    }

    public Player(String name, int eloPoint, double totalPoints) {
        this.name = name;
        this.eloPoint = eloPoint;
        this.gamesPlayed = 0;
        this.gamesWon = 0;
        this.gamesDraw = 0;
        this.gamesLost = 0;
        this.opponentList = FXCollections.observableArrayList();
        this.totalPoints = totalPoints;
    }

    public String getName() {
        return name;
    }

    public int getEloPoint() {
        return eloPoint;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public int getGamesLost() {
        return gamesLost;
    }

    public int getGamesDraw() {
        return gamesDraw;
    }

    @Override
    public String toString() {
        return this.getName() + " " + this.getEloPoint() + " " + this.getPlayerLevel();
    }

    public int playWithPlayer(Player opponent) {
        try {
            if (this.getEloPoint() + (rand.nextInt(500) + 1) > opponent.getEloPoint() + (rand.nextInt(500) + 1)) {
                System.out.println(this.getName() + " győzött.");
                this.gamesPlayed++;
                opponent.gamesPlayed++;
                this.gamesWon++;
                opponent.gamesLost++;
                PlayerList.getPlayerList().getGamesPlayed().add(new Game(this, opponent, 1));
                this.setTotalPoints();
                opponent.setTotalPoints();
                return 1;
            } else if (this.getEloPoint() + (rand.nextInt(500) + 1) < opponent.getEloPoint() + (rand.nextInt(500) + 1)) {
                System.out.println(opponent.getName() + " győzött.");
                this.gamesPlayed++;
                opponent.gamesPlayed++;
                this.gamesLost++;
                opponent.gamesWon++;
                PlayerList.getPlayerList().getGamesPlayed().add(new Game(this, opponent, -1));
                this.setTotalPoints();
                opponent.setTotalPoints();
                return -1;
            } else {
                System.out.println("Döntetlen.");
                this.gamesPlayed++;
                opponent.gamesPlayed++;
                this.gamesDraw++;
                opponent.gamesDraw++;
                PlayerList.getPlayerList().getGamesPlayed().add(new Game(this, opponent, 0));
                this.setTotalPoints();
                opponent.setTotalPoints();
                return 0;
            }

        } catch(NullPointerException e) {
            System.out.println("Nincs kivel játszani.");
            return -10;
        }

    }


}
