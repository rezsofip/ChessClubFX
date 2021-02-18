package datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static java.util.Collections.reverse;

public class PlayerList {

    private static PlayerList playerList = new PlayerList();
    private ObservableList<Player> players = FXCollections.observableArrayList();
    private ObservableList<Game> gamesPlayed = FXCollections.observableArrayList();

    public ObservableList<Game> getGamesPlayed() {
        return gamesPlayed;
    }

    public static PlayerList getPlayerList() {
        return playerList;
    }

    public ObservableList<Player> getPlayers() {
        return players;
    }

    public boolean addPlayer(String playerName, int eloPoint, String playerLevel) {
        if(this.findPlayer(playerName)) {
            System.out.println("A játékos már szerepel a listában.");
            return false;
        } else {
            players.add(new Player(playerName, eloPoint, playerLevel));
            return true;
        }
    }

    private boolean findPlayer(String playerName) {
        for(int i=0; i<players.size(); i++) {
            if(players.get(i).getName().equals(playerName)) {
                return true;
            }
        }

        return false;
    }

    public Player findPlayerObject(String playerName) {
        for(int i=0; i<players.size(); i++) {
            if(players.get(i).getName().equals(playerName)) {
                return players.get(i);
            }
        }

        return null;
    }

    public int findPlayerIndex(Player player) {
        for(int i=0; i<players.size(); i++) {
            if(players.get(i) == player) {
                return i;
            }
        }
        return -1;
    }

    public void addOpponentToOpponentList(Player opponentPlayer) {
        for(int i=0; i<PlayerList.getPlayerList().getPlayers().size(); i++) {
            if (PlayerList.getPlayerList().getPlayers().get(i) != opponentPlayer) {
                PlayerList.getPlayerList().getPlayers().get(i).getOpponentList().add(opponentPlayer);
                opponentPlayer.getOpponentList().add(PlayerList.getPlayerList().getPlayers().get(i));
            }
        }

    }

    public void deleteRemovedPlayerFromOpponents(Player removedPlayer) {
        for(Player player : PlayerList.getPlayerList().getPlayers()) {
            player.getOpponentList().remove(removedPlayer);
        }
    }

    public void savePlayers() {
BufferedWriter bufferedWriter = null;
        try{
            bufferedWriter = new BufferedWriter(new FileWriter("players.txt"));
            for(Player player : this.getPlayers()) {
                bufferedWriter.write(player.getName() + ", " + player.getEloPoint() + ", " + player.getPlayerLevel() + "\n");
            }
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedWriter.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
            }
    }

    public void savePlayers(Path path) {
        BufferedWriter bufferedWriter = null;
        try{
            bufferedWriter = new BufferedWriter(new FileWriter(path.toFile()));
            for(Player player : this.getPlayers()) {
                bufferedWriter.write(player.getName() + ", " + player.getEloPoint() + ", " + player.getPlayerLevel() + "\n");
            }
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedWriter.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveGames() {
        BufferedWriter bufferedWriter = null;

        try{
            bufferedWriter = new BufferedWriter(new FileWriter("games.txt"));
            for(Game game : this.getGamesPlayed()) {
                bufferedWriter.write(game.getPlayer().getName() + ", " + game.getOpponentPlayer().getName() + ", " + game.getWinner() + "\n");
            }
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedWriter.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveGames(Path path) {
        BufferedWriter bufferedWriter = null;

        try{
            bufferedWriter = new BufferedWriter(new FileWriter(path.toFile()));
            for(Game game : this.getGamesPlayed()) {
                bufferedWriter.write(game.getPlayer().getName() + ", " + game.getOpponentPlayer().getName() + ", " + game.getWinner() + "\n");
            }
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedWriter.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadPlayers() {
        Scanner scanner = null;

        try{
            scanner = new Scanner(new BufferedReader(new FileReader("players.txt")));
            scanner.useDelimiter(", ");
            while(scanner.hasNextLine())  {
                String input = scanner.nextLine();
                String [] data = input.split(", ");
                String playerName = data[0];
                int playerElo = Integer.parseInt(data[1]);
                String playerLevel = data[2];
                Player newPlayer = new Player(playerName, playerElo, playerLevel);
                this.getPlayers().add(newPlayer);
                PlayerList.getPlayerList().addOpponentToOpponentList(newPlayer);
            }
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if(scanner != null) {
                scanner.close();
            }
        }
    }

    public void removeAllPlayers() {
        PlayerList.getPlayerList().getPlayers().removeAll();
    }

    public void removeAllOpponents() {
        for(Player player : PlayerList.playerList.getPlayers()) {
            player.getOpponentList().removeAll();
        }
    }

    public void removeAllGames() {
        PlayerList.getPlayerList().getGamesPlayed().removeAll();
    }

    public void loadPlayers(Path path) {
        Scanner scanner = null;

        removeAllPlayers();
        removeAllOpponents();

        try{
            scanner = new Scanner(new BufferedReader(new FileReader(path.toFile())));
            scanner.useDelimiter(", ");
            while(scanner.hasNextLine())  {
                String input = scanner.nextLine();
                String [] data = input.split(", ");
                String playerName = data[0];
                int playerElo = Integer.parseInt(data[1]);
                String playerLevel = data[2];
                Player newPlayer = new Player(playerName, playerElo, playerLevel);
                this.getPlayers().add(newPlayer);
                PlayerList.getPlayerList().addOpponentToOpponentList(newPlayer);
            }
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if(scanner != null) {
                scanner.close();
            }
        }
    }

    public void loadGames() {
        Scanner scanner = null;

        try{
            scanner = new Scanner(new BufferedReader(new FileReader("games.txt")));
            scanner.useDelimiter(", ");
            while(scanner.hasNextLine())  {
                String input = scanner.nextLine();
                String [] data = input.split(", ");
                String playerName = data[0];
                String opponentPlayerName = data[1];
                int result = Integer.parseInt(data[2]);
                Player newPlayer = new Player(playerName);
                Player newOpponentPlayer = new Player(opponentPlayerName);
                PlayerList.getPlayerList().getGamesPlayed().add(new Game(newPlayer, newOpponentPlayer, result));
            }
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if(scanner != null) {
                scanner.close();
            }
        }
    }

    public void loadGames(Path path) {
        Scanner scanner = null;
        removeAllGames();

        try{
            scanner = new Scanner(new BufferedReader(new FileReader(path.toFile())));
            scanner.useDelimiter(", ");
            while(scanner.hasNextLine())  {
                String input = scanner.nextLine();
                String [] data = input.split(", ");
                String playerName = data[0];
                String opponentPlayerName = data[1];
                int result = Integer.parseInt(data[2]);
                Player newPlayer = new Player(playerName);
                Player newOpponentPlayer = new Player(opponentPlayerName);
                PlayerList.getPlayerList().getGamesPlayed().add(new Game(newPlayer, newOpponentPlayer, result));
            }
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if(scanner != null) {
                scanner.close();
            }
        }
    }

    public ObservableList<Player> sortPlayersByTotalPoints() {
        ObservableList<Player> newPlayerList = FXCollections.observableArrayList();
        newPlayerList = this.players.sorted(new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                if(o1.getTotalPoints() == o2.getTotalPoints()) {
                    return o1.getName().compareTo(o2.getName());
                } else if(o1.getTotalPoints() > o2.getTotalPoints()) {
                    return -1;
                } else {
                    return 1;
                }

            }
        });

        return newPlayerList;
    }

    public Map<String, Player> getRangList() {
        Map<String, Player> rangList = new HashMap<>();
        int i = 1;
        ListIterator listIterator = this.players.listIterator();
        while (listIterator.hasNext()) {
                rangList.put(String.valueOf(i) + ".", (Player)listIterator.next());
                i++;
            }


        return rangList;
    }

    public int playTournament() {
        if(players.size() == 0) {
            return -2;
        }
        if(players.size() == 1) {
            return -1;
        }

        for(int i=0; i<players.size(); i++) {
            for(int j=i; j<players.size(); j++) {
                if(players.get(i) != players.get(j)) {
                    System.out.println("i = " + i + " j = " + j);
                    players.get(i).playWithPlayer(players.get(j));
                }
            }
        }

        return 0;
    }

    public void saveRanglist() {
        ObservableList<Player> rangList = FXCollections.observableArrayList();
        rangList = PlayerList.getPlayerList().sortPlayersByTotalPoints();
        Path path = FileSystems.getDefault().getPath("ranglist.txt");
        BufferedWriter bufferedWriter = null;
        try{
            bufferedWriter = new BufferedWriter(new FileWriter(path.toFile()));
            for(int i=0; i< rangList.size(); i++) {
                bufferedWriter.write(String.valueOf(rangList.get(i).getTotalPoints()) + ",");
                bufferedWriter.write(rangList.get(i).getName()+ ",");
                bufferedWriter.write(String.valueOf(rangList.get(i).getEloPoint()));
                if(i != rangList.size() - 1) {
                    bufferedWriter.write("\n");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try{
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public void saveRanglist(Path path) {
        ObservableList<Player> rangList = FXCollections.observableArrayList();
        rangList = PlayerList.getPlayerList().sortPlayersByTotalPoints();
        BufferedWriter bufferedWriter = null;
        try{
            bufferedWriter = new BufferedWriter(new FileWriter(path.toFile()));
            for(int i=0; i< rangList.size(); i++) {
                bufferedWriter.write(String.valueOf(rangList.get(i).getTotalPoints()) + ",");
                bufferedWriter.write(rangList.get(i).getName()+ ",");
                bufferedWriter.write(String.valueOf(rangList.get(i).getEloPoint()));
                if(i != rangList.size() - 1) {
                    bufferedWriter.write("\n");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try{
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public void loadRanglistAndPlayers() {
        Path path = FileSystems.getDefault().getPath("ranglist.txt");
        BufferedReader bufferedReader = null;
        if(Files.exists(path)) {
            try {
                bufferedReader = new BufferedReader(new FileReader(path.toFile()));
                String input = bufferedReader.readLine();
                while(input != null) {
                    String [] content = input.split(",");
                    double totalPoints = Double.parseDouble(content[0]);
                    String name = content[1];
                    int eloPoints = Integer.parseInt(content[2]);
                    Player player = new Player(name, eloPoints, totalPoints);
                    players.add(player);
                    input = bufferedReader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try{
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            for(int i=0; i<players.size(); i++) {
                System.out.println(players.get(i).getTotalPoints());
            }
        }
    }

    public void loadRanglistAndPlayers(Path path) {
        BufferedReader bufferedReader = null;
        if(Files.exists(path)) {
            try {
                bufferedReader = new BufferedReader(new FileReader(path.toFile()));
                String input = bufferedReader.readLine();
                while(input != null) {
                    String [] content = input.split(",");
                    double totalPoints = Double.parseDouble(content[0]);
                    String name = content[1];
                    int eloPoints = Integer.parseInt(content[2]);
                    Player player = new Player(name, eloPoints, totalPoints);
                    players.add(player);
                    input = bufferedReader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try{
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            for(int i=0; i<players.size(); i++) {
                System.out.println(players.get(i).getTotalPoints());
            }
        }
    }

    }





