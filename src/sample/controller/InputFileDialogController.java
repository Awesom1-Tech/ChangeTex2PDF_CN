package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.Main;


import java.io.IOException;

public class InputFileDialogController {

    static Stage stage = new Stage();

    public Stage loadDialog() throws IOException {
        Scene dialogScene = new Scene(FXMLLoader.load(getClass().getResource("../view/inputFileDialog.fxml")));
        stage.setScene(dialogScene);
        return stage;
    }

    @FXML
    public void closeDialog(){
        stage.close();
        Main.getMainStage().show();
    }
}
