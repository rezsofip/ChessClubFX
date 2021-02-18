package sample;

import datamodel.Player;
import datamodel.PlayerList;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;

public class Controller {

    @FXML
    private TextField playerName;
    @FXML
    private Button add;
    @FXML
    private Label message;
    @FXML
    private ListView<Player> playerList;
    @FXML
    private Button delete;
    @FXML
    private ListView<Player> opponentList;
    @FXML
    private ToggleButton filterAdvancedPlayers;
    @FXML
    private ToggleButton sortListByName;
    private FilteredList<Player> filteredList;
    private SortedList<Player> sortedListByName;
    private SortedList<Player> sortedListByEloPoint;
    @FXML
    private BorderPane borderPane;
    @FXML
    private CheckBox savePlayers;
    @FXML
    private CheckBox saveGames;
    @FXML
    private TextField eloPoint;
    @FXML
    private CheckBox saveRanglist;
    @FXML
    private ToggleButton sortListByEloPoint;
    @FXML
    private ComboBox<String> playerLevelComboBox;

    public void initialize() {

        File playerFile = new File("players.txt");
        File gamesFile = new File("games.txt");
        Path path = FileSystems.getDefault().getPath("ranglist.txt");

        if(playerFile.exists() && Files.exists(path)) {
            PlayerList.getPlayerList().loadRanglistAndPlayers();
        } else if(playerFile.exists() && !Files.exists(path)) {
            PlayerList.getPlayerList().loadPlayers();
        }

        if(gamesFile.exists()) {
            PlayerList.getPlayerList().loadGames();
        }


playerList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

opponentList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

filteredList = new FilteredList<>(PlayerList.getPlayerList().getPlayers(), new Predicate<Player>() {
    @Override
    public boolean test(Player player) {
        return true;
    }
});         // alapesetben minden játékos megjelenik

sortedListByName = new SortedList<>(filteredList, new Comparator<Player>() {
    @Override
    public int compare(Player o1, Player o2) {
        return o1.getName().compareTo(o2.getName());
    }
});           // a rendezés név szerint történik
sortedListByEloPoint = new SortedList<>(filteredList, new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                if(o1.getEloPoint() > o2.getEloPoint()) {
                    return 1;
                } else if(o1.getEloPoint() < o2.getEloPoint()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });           // a rendezés név szerint történik
        playerList.setItems(filteredList);

        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem1 = new MenuItem("Törlés");
        menuItem1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                onDeleteButtonClicked();
            }
        });
        MenuItem menuItem2 = new MenuItem("Össze játékos törlése");
        menuItem2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                deleteAllPlayers();
            }
        });
        contextMenu.getItems().addAll(menuItem1);
        contextMenu.getItems().addAll(menuItem2);
        playerList.setContextMenu(contextMenu);

        playerLevelComboBox.setPromptText("Kérem válasszon egy szintet...");
        playerLevelComboBox.getItems().add("Kezdő");
        playerLevelComboBox.getItems().add("Haladó");
    }

    public void onAddButtonClicked() {        // Validáció és játékos hozzáadás a listához
            try {
                if (playerName.getText().isEmpty() || playerName.getText().trim().isEmpty()) {
                    message.setText("Kérem adjon meg egy nevet a játékosnak.");
                } else {
                    if (eloPoint.getText().isEmpty()) {
                        message.setText("Kérem adja meg az Élő pontszámot a játékosnak!");
                    }else {
                        try{
                            int eloPointValue = Integer.parseInt(eloPoint.getText());
                            String foundPlayer = playerName.getText();
                            if(eloPointValue < 0 || eloPointValue > 3000) {
                                message.setText("Kérem egy 0 és 3000 közötti egész számot adjon meg Élő pontnak!");
                                return;
                            }
                            String playerLevel = playerLevelComboBox.getSelectionModel().getSelectedItem();
                            if(playerLevel == null) {
                                message.setText("Kérem válasszon ki egy szintet a játékosnak!");
                                return;
                            }
                            if (PlayerList.getPlayerList().addPlayer(foundPlayer, eloPointValue, playerLevel)) {
                                PlayerList.getPlayerList().addOpponentToOpponentList(PlayerList.getPlayerList().findPlayerObject(foundPlayer));
                                this.message.setText("A játékos sikeresen hozzáadva.");
                            } else {
                                this.message.setText("Ez a játékos már megtalálható a listában.");
                            }

                            playerName.clear();
                            eloPoint.clear();
                        }catch (NumberFormatException e) {
                            message.setText("Kérem adjon meg egy egész számot az Élő pontszámnak");
                        }
                    }
                }
                } catch(NumberFormatException e){
                    message.setText("Kérem adjon meg egy számot!");
                }

            }
    public void onSelectedItem() {  // az ellenfeleknél mindig a kiválasztott játékos ellenfeleit mutatjuk
        opponentList.setItems(playerList.getSelectionModel().getSelectedItem().getOpponentList());
        }

    public void onFilterAction() {  // itt megadjuk, hogy mi történjen szűréskor
        if (filterAdvancedPlayers.isSelected()) {
            filteredList.setPredicate(new Predicate<Player>() {
                @Override
                public boolean test(Player player) {
                    if (player.getPlayerLevel().equals("Haladó")) {
                        return true;
                    } else {
                        return false;
                    }
                }
            });
        } else {
            filteredList.setPredicate(new Predicate<Player>() {
                @Override
                public boolean test(Player player) {

                    return true;
                }
            });
        }
    }

    public void onSortByNameAction() {  // megadjuk mi történjen rendezéskor
        if(sortListByEloPoint.isSelected()) {
            message.setText("Nem lehet egyszerre név és élő pont szerint sorba rendezni.");
            sortListByName.setSelected(false);
        } else {
            if(sortListByName.isSelected()) {
                playerList.setItems(sortedListByName);
            } else {
                playerList.setItems(filteredList);
            }
        }

    }

    public void onSortByEloPointAction() {
        if(sortListByName.isSelected()) {
            message.setText("Nem lehet egyszerre név és élő pont szerint sorba rendezni.");
            sortListByEloPoint.setSelected(false);
        } else {
            if(sortListByEloPoint.isSelected()) {
                playerList.setItems(sortedListByEloPoint);
            } else {
                playerList.setItems(filteredList);
            }
        }
    }



    public void onDeleteButtonClicked() {  // megadjuk mi történjen játékos törléskor
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Játékos eltávolítása");
        alert.setHeaderText(playerList.getSelectionModel().getSelectedItem().getName() + " eltávolítása");
        alert.setContentText("Biztos benne, hogy el akarja távolítani " + playerList.getSelectionModel().getSelectedItem().getName() + "-t a listából?\nNyomja meg az OK gombot, ha igen,\nha nem nyomja meg a Cancel gombot");
        Optional<ButtonType> result = alert.showAndWait();

        int indexOfDeletedItem = 0;

        if(result.isPresent() && (result.get() == ButtonType.OK)) {
            PlayerList.getPlayerList().deleteRemovedPlayerFromOpponents(playerList.getSelectionModel().getSelectedItem());
            // először az ellenfelek közül töröljük
             indexOfDeletedItem = PlayerList.getPlayerList().findPlayerIndex(playerList.getSelectionModel().getSelectedItem());
            PlayerList.getPlayerList().getPlayers().remove(playerList.getSelectionModel().getSelectedItem());
            // aztán a játékosok közül is
            this.message.setText("A játékos sikeresen törölve.");
        }

        playerList.getSelectionModel().select(PlayerList.getPlayerList().getPlayers().get(indexOfDeletedItem -1)); // frissítjük a két lista tartalmát

    }

    public void playGame() { // labelbe kiírjuk az eredményt
        int result;
        try {
       result = playerList.getSelectionModel().getSelectedItem().playWithPlayer(opponentList.getSelectionModel().getSelectedItem());
            if (result == 0) {
                message.setText("Döntetlen.");
            } else if (result == 1) {
                message.setText(playerList.getSelectionModel().getSelectedItem().getName() + " győzött.");
            } else if (result == -1) {
                message.setText(opponentList.getSelectionModel().getSelectedItem().getName() + " győzött.");
            } else {
                message.setText("Nincs kivel játszani.");
            }
        } catch(NullPointerException e) {
            message.setText("Kérem jelöljön ki egy játékost!");
        }
    }

    @FXML
    public void showGamesHistory() { // megjelenítjük az eredményeket egy új ablakban
        try {
            Parent root = FXMLLoader.load(getClass().getResource("GameHistory.fxml"));
            Stage newStage = new Stage();
            newStage.setTitle("Game History");
            newStage.setScene(new Scene(root, 600, 600));
            newStage.initOwner(borderPane.getScene().getWindow());
            newStage.show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public boolean onSavePlayersCheckbox() {

        if(!savePlayers.isSelected()) {
            saveRanglist.setSelected(false);
        }


        // ez azért kell, hogy más osztályból meg tudjuk állapítani, hogy be van-e nyomva a mentés checkbox
        if(savePlayers.isSelected()) {
return true;
        } else {
            return false;
        }
    }

    public boolean onSaveGamesCheckbox() {
        // ez azért kell, hogy más osztályból meg tudjuk állapítani, hogy be van-e nyomva a mentés checkbox
if(saveGames.isSelected()) {
    return true;
} else {
    return false;
}
    }

    @FXML
    public void showRangList() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Ranglist.fxml"));
            Stage newStage = new Stage();
            newStage.setTitle("Ranglist");
            newStage.initOwner(borderPane.getScene().getWindow());
            newStage.setScene(new Scene(root,600, 600));
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void playTournament() {

        if(PlayerList.getPlayerList().playTournament() == 0) {
            message.setText("Körjátszma sikeresen lezárult.");

        } else if(PlayerList.getPlayerList().playTournament() == -1) {
            message.setText("Csak egy játékos van.");

        } else if(PlayerList.getPlayerList().playTournament() == -2){
            message.setText("Nincs egyetlen játékos sem.");

        }
    }

    @FXML
    public void deleteAllPlayers() {
        PlayerList.getPlayerList().getPlayers().removeAll(PlayerList.getPlayerList().getPlayers());
        message.setText("A játékosok sikeresen törlésre kerültek.");
    }

    @FXML
    public void deleteGameHistory() {
        PlayerList.getPlayerList().getGamesPlayed().removeAll(PlayerList.getPlayerList().getGamesPlayed());
        message.setText("Az eddig lejátszott játszmák sikeresen törlésre kerültek.");
    }


    @FXML
    public boolean onSaveRanglistCheckbox() {

       if(saveRanglist.isSelected()) {
           savePlayers.setSelected(true);
       }

        if(saveRanglist.isSelected()) {
            return true;
        } else {
            return false;
        }
    }

    @FXML
    public void quit() {
        Platform.exit();
    }

    @FXML
    public void jumpToFileSaveWindow() {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("SaveToFile.fxml"));
            Stage newStage = new Stage();
            newStage.setTitle("Save Window");
            newStage.setScene(new Scene(root, 600, 600));
            newStage.show();

        }catch(IOException e) {
            message.setText("Could not load the Save Window");
        }
    }

    @FXML
    public void jumpToFileLoadWindow() {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("LoadFile.fxml"));
            Stage newStage = new Stage();
            newStage.setTitle("Load Window");
            newStage.setScene(new Scene(root, 600, 600));
            newStage.show();

        }catch(IOException e) {
            message.setText("Could not load the Load Window");
        }
    }

}
