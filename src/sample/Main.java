package sample;

import datamodel.PlayerList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main extends Application {

    private FXMLLoader loader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
    private Controller controller;

    // Ezek azért kellenek, hogy később tudjak rájuk hivatkozni

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = loader.load();
        controller = loader.getController();
        primaryStage.setTitle("Chess Club");
        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {

        // kilépéskor először törli a fájlokat, utána elmenti a fájlba az adatokat, ha kérve volt a mentés

        if (controller.onSavePlayersCheckbox()) {
            File file = new File("players.txt");
            if (file.exists()) {
                file.delete();
            }
            PlayerList.getPlayerList().savePlayers();
        } else {
            File file = new File("players.txt");
            if (file.exists()) {
                file.delete();
            }
        }

        if (controller.onSaveGamesCheckbox()) {


            File file = new File("games.txt");
            if (file.exists()) {
                file.delete();
            }
            PlayerList.getPlayerList().saveGames();

        }else {
            File file = new File("games.txt");
            if (file.exists()) {
                file.delete();
            }
        }

        if(controller.onSaveRanglistCheckbox()){
            PlayerList.getPlayerList().saveRanglist();
        } else {
            Path path = FileSystems.getDefault().getPath("ranglist.txt");
            if(Files.exists(path)) {
                Files.delete(path);
            }
        }

            super.stop();
        }



    public static void main(String[] args) {
        launch(args);
    }
}
