package sample;

import datamodel.PlayerList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.nio.file.Path;

public class LoadFileController {

    private Path loadPlayerPath;
    private Path loadGamePath;
    private Path loadRanglistPath;

    @FXML
    private GridPane gridPane;
    @FXML
    private CheckBox loadPlayersCheckbox;
    @FXML
    private CheckBox loadGamesCheckbox;
    @FXML
    private CheckBox loadRanglistCheckbox;
    @FXML
    private Label messageLabel;

    @FXML
    public void openFileChooserPlayer() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Adja meg a játékosok fájl elérési útvonalát!");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text fájl", "*.txt"));
        try{
            Path selectedPath = fileChooser.showOpenDialog(gridPane.getScene().getWindow()).toPath();
            loadPlayerPath = selectedPath;
        } catch (NullPointerException e) {

        }


    }
    @FXML
    public void openFileChooserGame() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Adja meg a játszmák fájl elérési útvonalát");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text fájl", "*.txt"));
        try{
            Path selectedPath = fileChooser.showOpenDialog(gridPane.getScene().getWindow()).toPath();
            loadGamePath = selectedPath;
        } catch (NullPointerException e) {

        }

    }
    @FXML
    public void openFileChooserRanglist() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Adja meg a ranglista fájl elérési útvonalát!");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text fájl", "*.txt"));
        try{
            Path selectedPath = fileChooser.showOpenDialog(gridPane.getScene().getWindow()).toPath();
            loadRanglistPath = selectedPath;
        } catch (NullPointerException e) {

        }

    }

    @FXML
    public void closeLoadToFileWindow() {
        Stage currentStage = (Stage) gridPane.getScene().getWindow();
        currentStage.close();
    }

    @FXML
    public void loadFile() {
        if(loadGamesCheckbox.isSelected()) {
            PlayerList.getPlayerList().loadGames(this.loadGamePath);
        }
        if(loadRanglistCheckbox.isSelected() && loadPlayersCheckbox.isSelected() ) {
            PlayerList.getPlayerList().loadRanglistAndPlayers(this.loadRanglistPath);
        } else if(loadPlayersCheckbox.isSelected() && loadRanglistCheckbox.isSelected()) {
            PlayerList.getPlayerList().loadPlayers(this.loadPlayerPath);
        }

        if(loadPlayersCheckbox.isSelected() || loadGamesCheckbox.isSelected() || loadRanglistCheckbox.isSelected()) {
            messageLabel.setText("Sikeres betöltés!");
        } else {
            messageLabel.setText("Kérem jelölje be mely adatokat kívánja betölteni!");
        }


    }

    @FXML
    public void checkLoadPlayersCheckbox() {
        if(loadRanglistCheckbox.isSelected()) {
            loadPlayersCheckbox.setSelected(true);
        }
    }

    @FXML
    public void unCheckLoadRanglistCheckbox() {
        if(!loadPlayersCheckbox.isSelected()) {
            loadRanglistCheckbox.setSelected(false);
        }
    }

}
