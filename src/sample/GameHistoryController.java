package sample;

import datamodel.Game;
import datamodel.PlayerList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class GameHistoryController {

    @FXML
    private ListView<Game> gamesListView;

    public void initialize() {
        gamesListView.setItems(PlayerList.getPlayerList().getGamesPlayed());
    }


}
