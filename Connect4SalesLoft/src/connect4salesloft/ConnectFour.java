/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connect4salesloft;

import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author David
 */
public class ConnectFour extends Application {
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent rootMain = FXMLLoader.load(getClass().getResource("ConnectFour.fxml"));
        Scene scene = new Scene(rootMain);

        //scene.getStylesheets().add(getClass().getResource("AlternativesStyleSheet.css").toExternalForm());  
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setMinWidth(1200);
        //primaryStage.getIcons().add(new Image(Alternatives.class.getResourceAsStream("/images/software_green_white.jpg")));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
