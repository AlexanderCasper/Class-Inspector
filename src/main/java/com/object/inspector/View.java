package com.object.inspector;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class View extends Application {

    public final static String idSuchfeld = "suchfeld";
    public final static String idButton = "button";
    public final static String idinfoBox = "infobox";
    
    private final Controller controller = new Controller();
    
    
    
    
    @Override
    public void start(Stage stage) {
        BorderPane borderPane = new BorderPane();
        HBox topBox = new HBox();
        
        TextField suchfeld = new TextField();
        suchfeld.setPrefColumnCount (25);
        suchfeld.setPromptText("Enter Classpath");
        
        
        Button button = new Button("Laden");
        ListView<String> infoBox = new ListView<>(controller.getInput());
        
        
        
        suchfeld.setId(idSuchfeld);
        infoBox.setId(idinfoBox);
        button.setId(idButton);
        
        
        
        button.setOnAction(controller::action);
        suchfeld.setOnKeyReleased(controller::keyRelease);
        
        topBox.getChildren().add(suchfeld);
        topBox.getChildren().add(button);
        borderPane.setCenter(infoBox);
        borderPane.setTop(topBox);
        
        

        Scene scene = new Scene(borderPane,1000, 500);
        stage.setScene(scene);
        stage.show();
    }


    
    
    public static void main(String[] args) {
        launch();
    }

}