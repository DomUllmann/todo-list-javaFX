package application;

import java.net.URL;

import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		String fxmlPath = "ToDoGUI.fxml";
		URL location = getClass().getResource(fxmlPath);
		FXMLLoader loader = new FXMLLoader(location);

		Scene scene = new Scene(loader.load());
		primaryStage.setTitle("To-Do List Application");
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();
	
		
	}

	public static void main(String[] args) {
		launch(args);
	}
}
