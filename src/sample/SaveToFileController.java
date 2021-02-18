package sample;

import datamodel.PlayerList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.nio.file.Path;

public class SaveToFileController {

    private Path savePlayerPath;
    private Path saveGamePath;
    private Path saveRanglistPath;

    @FXML
    private GridPane gridPane;
    @FXML
    private CheckBox savePlayersCheckbox;
    @FXML
    private CheckBox saveGamesCheckbox;
    @FXML
    private CheckBox saveRanglistCheckbox;
    @FXML
    private Label messageLabel;

    @FXML
    public void openFileChooserPlayer() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Adja meg a játékosok fájl mentési helyét!");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text fájl", "*.txt"));
        try{
            Path selectedPath = fileChooser.showSaveDialog(gridPane.getScene().getWindow()).toPath();
            savePlayerPath = selectedPath;
        } catch (NullPointerException e) {

        }


    }
    @FXML
    public void openFileChooserGame() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Adja meg a játszmák fájl mentési helyét!");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text fájl", "*.txt"));
        try{
            Path selectedPath = fileChooser.showSaveDialog(gridPane.getScene().getWindow()).toPath();
            saveGamePath = selectedPath;
        } catch (NullPointerException e) {

        }

    }
    @FXML
    public void openFileChooserRanglist() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Adja meg a ranglista fájl mentési helyét!");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text fájl", "*.txt"));
        try{
            Path selectedPath = fileChooser.showSaveDialog(gridPane.getScene().getWindow()).toPath();
            saveRanglistPath = selectedPath;
        } catch (NullPointerException e) {

        }

    }

    @FXML
    public void closeSaveToFileWindow() {
        Stage currentStage = (Stage) gridPane.getScene().getWindow();
        currentStage.close();
    }

    @FXML
    public void saveToFile() {
        if(savePlayersCheckbox.isSelected()) {
            PlayerList.getPlayerList().savePlayers(this.savePlayerPath);
        }
        if(saveGamesCheckbox.isSelected()) {
            PlayerList.getPlayerList().saveGames(this.saveGamePath);
        }
        if(saveRanglistCheckbox.isSelected()) {
            PlayerList.getPlayerList().saveRanglist(this.saveRanglistPath);
        }

        if(savePlayersCheckbox.isSelected() || saveGamesCheckbox.isSelected() || saveRanglistCheckbox.isSelected()) {
            messageLabel.setText("Sikeres mentés!");
        } else {
            messageLabel.setText("Kérem jelölje be mely adatokat kívánja menteni!");
        }


    }
}
