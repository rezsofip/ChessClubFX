package sample;

import datamodel.Player;
import datamodel.PlayerList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class RanglistController {

    @FXML
    private TableView<Player> rangList;

    public void initialize() {
        TableColumn totalPoints = new TableColumn("Pontok");
        TableColumn playerName = new TableColumn("Név");
        TableColumn eloPoint = new TableColumn("Élő pontszám");

        rangList.getColumns().add(totalPoints);
        rangList.getColumns().add(playerName);
        rangList.getColumns().add(eloPoint);

        totalPoints.setCellValueFactory(new PropertyValueFactory<Player, Double>("totalPoints"));
        playerName.setCellValueFactory(new PropertyValueFactory<Player, String>("name"));
        eloPoint.setCellValueFactory(new PropertyValueFactory<Player, Integer>("eloPoint"));

        rangList.setItems(PlayerList.getPlayerList().sortPlayersByTotalPoints());
    }

}
